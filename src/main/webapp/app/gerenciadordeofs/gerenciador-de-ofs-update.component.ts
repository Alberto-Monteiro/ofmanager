import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GerenciadorDeOfsService } from './gerenciador-de-ofs.service';
import { IOrdemFornecimento } from 'app/shared/model/ordem-fornecimento.model';
import { Arquivo, IArquivo } from 'app/shared/model/arquivo.model';
import { ArquivoDaOf, IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import * as fileSaver from 'file-saver';
import { IUser } from 'app/core/user/user.model';
import { IServicoOf } from 'app/shared/model/servico-of.model';

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
      this.gerenciadorDeOfsService.getUsuariosGestor().subscribe(usuarios => {
        this.usuariosGestor = usuarios.body;
        this.updateForm(ordemFornecimento);
      });
    });
  }

  updateForm(ordemFornecimento: IOrdemFornecimento): void {
    this.editForm.patchValue({
      usuarioGestor: this.usuariosGestor!.filter(value => value.id === ordemFornecimento.servicoOf!.gestorDaOf!.id)![0],
      numero: ordemFornecimento.servicoOf!.numero,
      listaDeArquivo: ordemFornecimento.listaDosArquivos
    });
  }

  private createFromForm(): IOrdemFornecimento {
    return {
      listaDosArquivos: this.editForm.get(['listaDeArquivo'])!.value,
      servicoOf: {
        id: this.ordemFornecimento!.servicoOf!.id,
        gestorDaOf: this.editForm.get(['usuarioGestor'])!.value,
        numero: this.editForm.get(['numero'])!.value
      }
    };
  }

  previousState(): void {
    window.history.back();
  }

  processar(): void {
    this.gerenciadorDeOfsService.processar(this.createFromForm()).subscribe(ordemFornecimento => {
      this.ordemFornecimento = ordemFornecimento.body!;
      this.updateForm(this.ordemFornecimento);
    });
  }

  isArquivoTest(arquivo: IArquivo): void {
    arquivo.arquivoDeTest = !arquivo.arquivoDeTest;
    this.gerenciadorDeOfsService.updateIsTestArquivo(arquivo).subscribe(
      () => {},
      () => {
        arquivo.arquivoDeTest = !arquivo.arquivoDeTest;
      }
    );
  }

  atualizaComplexidade(arquivo: Arquivo, complexidade: any): void {
    const complexidadeAnterior = arquivo.complexidade;
    arquivo.complexidade = complexidade;
    this.gerenciadorDeOfsService.updateComplexidade(arquivo).subscribe(
      () => {},
      () => {
        arquivo.complexidade = complexidadeAnterior;
      }
    );
  }

  atualizaEstadoArquivo(arquivoDaOf: ArquivoDaOf, estadoArquivo: any): void {
    const estadoArquivoAnterior = arquivoDaOf.estadoArquivo;
    arquivoDaOf.estadoArquivo = estadoArquivo;
    this.gerenciadorDeOfsService.updateEstadoArquivo(arquivoDaOf).subscribe(
      () => {},
      () => {
        arquivoDaOf.estadoArquivo = estadoArquivoAnterior;
      }
    );
  }

  deletarArquivoDaOf(arquivoDaOf: IArquivoDaOf): void {
    this.gerenciadorDeOfsService.deletarArquivoDaOf(arquivoDaOf).subscribe(ordemFornecimento => {
      this.ordemFornecimento = ordemFornecimento.body!;
      this.updateForm(this.ordemFornecimento);
    });
  }

  download(): void {
    this.gerenciadorDeOfsService.downloadPlanilha(this.ordemFornecimento!.servicoOf!.id).subscribe(response => {
      const blob: any = new Blob([response], { type: 'text/xlsx' });
      fileSaver.saveAs(blob, `OF-${this.ordemFornecimento!.servicoOf!.numero}.xlsx`);
    });
  }

  podeSerArquivoDeTest(extensao: string): boolean {
    return extensao === 'java' || extensao === 'js' || extensao === 'ts';
  }

  atualizaEstadoDaOf(servicoOf: IServicoOf, estado: any): void {
    const estadoAnterior = servicoOf.estado;
    servicoOf.estado = estado;
    this.gerenciadorDeOfsService.updateEstadoDaOf(servicoOf).subscribe(
      servicoOf1 => {
        servicoOf.lastModifiedDate = servicoOf1.body!.lastModifiedDate;
      },
      () => {
        servicoOf.estado = estadoAnterior;
      }
    );
  }

  downloadTxt(): void {
    this.gerenciadorDeOfsService.downloadTxt(this.ordemFornecimento!.servicoOf!.id).subscribe(response => {
      const blob: any = new Blob([response], { type: 'text/txt' });
      fileSaver.saveAs(blob, `OF-${this.ordemFornecimento!.servicoOf!.numero}.txt`);
    });
  }
}
