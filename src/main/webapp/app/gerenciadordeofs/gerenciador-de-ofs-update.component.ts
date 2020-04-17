import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GerenciadorDeOfsService } from './gerenciador-de-ofs.service';
import { IOrdemFornecimento } from 'app/shared/model/ordem-fornecimento.model';
import * as fileSaver from 'file-saver';
import { IUser } from 'app/core/user/user.model';
import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { Artefato, IArtefato } from 'app/shared/model/artefato.model';
import { ArtefatoOrdemDeFornecimento, IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

@Component({
  selector: 'of-gerenciador-de-ofs-update',
  templateUrl: './gerenciador-de-ofs-update.component.html'
})
export class GerenciadorDeOfsUpdateComponent implements OnInit {
  isSaving = false;
  ordemFornecimento?: IOrdemFornecimento;
  usuariosGestor?: IUser[] | null;

  editForm = this.fb.group({
    numero: [null, [Validators.required, Validators.max(999999999), Validators.min(100)]],
    usuarioGestor: [null, [Validators.required]],
    listaDeArquivo: [null, [Validators.required]]
  });

  constructor(
    protected gerenciadorDeOfsService: GerenciadorDeOfsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordemFornecimento }) => {
      this.ordemFornecimento = ordemFornecimento;
      this.gerenciadorDeOfsService.getUsuariosGestor().subscribe(response => {
        this.usuariosGestor = response.body;
        this.updateForm(ordemFornecimento);
      });
    });
  }

  updateForm(ordemFornecimento: IOrdemFornecimento): void {
    this.editForm.patchValue({
      usuarioGestor: this.usuariosGestor!.filter(
        value =>
          value.id === (ordemFornecimento.ordemDeFornecimento!.gestorDaOf! ? ordemFornecimento.ordemDeFornecimento!.gestorDaOf!.id : 0)
      )[0],
      numero: ordemFornecimento.ordemDeFornecimento!.numero,
      listaDeArquivo: ordemFornecimento.listaDosArquivos
    });
  }

  private createFromForm(): IOrdemFornecimento {
    return {
      listaDosArquivos: this.editForm.get(['listaDeArquivo'])!.value,
      ordemDeFornecimento: {
        id: this.ordemFornecimento!.ordemDeFornecimento!.id,
        gestorDaOf: this.editForm.get(['usuarioGestor'])!.value,
        numero: this.editForm.get(['numero'])!.value
      }
    };
  }

  previousState(): void {
    window.history.back();
  }

  atualizaGestorDaOf(): void {
    this.createFromForm();
    this.gerenciadorDeOfsService.updateGestorDaOf(this.createFromForm()).subscribe(response => {
      this.ordemFornecimento = response.body!;
      this.updateForm(this.ordemFornecimento);
    });
  }

  atualizaEstadoDaOf(ordemDeFornecimento: IOrdemDeFornecimento, estado: any): void {
    const estadoAnterior = ordemDeFornecimento.estado;
    ordemDeFornecimento.estado = estado;
    this.gerenciadorDeOfsService.updateEstadoDaOf(ordemDeFornecimento).subscribe(
      response => {
        ordemDeFornecimento.lastModifiedDate = response.body!.lastModifiedDate;
        ordemDeFornecimento.lastModifiedBy = response.body!.lastModifiedBy;
      },
      () => {
        ordemDeFornecimento.estado = estadoAnterior;
      }
    );
  }

  processar(): void {
    this.gerenciadorDeOfsService.processar(this.createFromForm()).subscribe(response => {
      this.ordemFornecimento = response.body!;
      this.updateForm(this.ordemFornecimento);
    });
  }

  podeSerArquivoDeTest(extensao: string): boolean {
    return extensao === 'java' || extensao === 'js' || extensao === 'ts';
  }

  isArquivoTest(artefato: IArtefato): void {
    artefato.artefatoDeTest = !artefato.artefatoDeTest;
    this.gerenciadorDeOfsService.updateIsTestArquivo(artefato).subscribe(
      () => {},
      () => {
        artefato.artefatoDeTest = !artefato.artefatoDeTest;
      }
    );
  }

  atualizaComplexidade(artefato: Artefato, complexidade: any): void {
    const complexidadeAnterior = artefato.complexidade;
    artefato.complexidade = complexidade;
    this.gerenciadorDeOfsService.updateComplexidade(this.ordemFornecimento!.ordemDeFornecimento!, artefato).subscribe(
      () => {},
      () => {
        artefato.complexidade = complexidadeAnterior;
      }
    );
  }

  atualizaEstadoArquivo(artefatoOrdemDeFornecimento: ArtefatoOrdemDeFornecimento, estadoArtefato: any): void {
    const estadoArquivoAnterior = artefatoOrdemDeFornecimento.estado;
    artefatoOrdemDeFornecimento.estado = estadoArtefato;
    this.gerenciadorDeOfsService.updateEstadoArquivo(artefatoOrdemDeFornecimento).subscribe(
      () => {},
      () => {
        artefatoOrdemDeFornecimento.estado = estadoArquivoAnterior;
      }
    );
  }

  deletarArquivoDaOf(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): void {
    this.gerenciadorDeOfsService.deletarArquivoDaOf(artefatoOrdemDeFornecimento).subscribe(response => {
      this.ordemFornecimento = response.body!;
      this.updateForm(this.ordemFornecimento);
    });
  }

  download(): void {
    this.gerenciadorDeOfsService.downloadPlanilha(this.ordemFornecimento!.ordemDeFornecimento!.id).subscribe(response => {
      const blob: Blob = new Blob([response], { type: 'text/xlsx' });
      fileSaver.saveAs(blob, `OF-${this.ordemFornecimento!.ordemDeFornecimento!.numero}.xlsx`);
    });
  }

  downloadTxt(): void {
    this.gerenciadorDeOfsService.downloadTxt(this.ordemFornecimento!.ordemDeFornecimento!.id).subscribe(response => {
      const blob: Blob = new Blob([response], { type: 'text/txt' });
      fileSaver.saveAs(blob, `OF-${this.ordemFornecimento!.ordemDeFornecimento!.numero}.txt`);
    });
  }

  getNomeDoArquivo(caminhoDoArquivo: string): string {
    const nomeDoArquivo: string[] = caminhoDoArquivo.split('/');
    return nomeDoArquivo[nomeDoArquivo.length - 1];
  }

  substituir(value1: string, value2: string): void {
    const listaDosArquivos = this.createFromForm()!
      .listaDosArquivos!.split(value1)
      .join(value2);
    this.editForm.patchValue({ listaDeArquivo: listaDosArquivos });
  }
}
