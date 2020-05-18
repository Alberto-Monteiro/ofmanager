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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LinhaArtefatoDeleteDialogComponent } from 'app/gerenciadordeofs/linha-artefato-delete-dialog.component';
import { JhiEventManager } from 'ng-jhipster';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import * as moment from 'moment';

@Component({
  selector: 'of-gerenciador-de-ofs-update',
  templateUrl: './gerenciador-de-ofs-update.component.html',
  styleUrls: ['./gerenciador-de-ofs.scss']
})
export class GerenciadorDeOfsUpdateComponent implements OnInit {
  isSaving = false;
  ordemFornecimento?: IOrdemFornecimento;
  usuariosGestor?: IUser[] | null;

  editForm = this.fb.group({
    numero: [null, [Validators.max(999999999), Validators.min(100)]],
    usuarioGestor: [null, [Validators.required]],
    listaDeArquivo: [null, [Validators.required]],
    ustibb: [],
    observacoes: [],
    dataDeEntrega: []
  });

  constructor(
    protected gerenciadorDeOfsService: GerenciadorDeOfsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected modalService: NgbModal,
    protected eventManager: JhiEventManager
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
      listaDeArquivo: ordemFornecimento.listaDosArquivos,
      ustibb: ordemFornecimento.ordemDeFornecimento!.valorUstibb,
      observacoes: ordemFornecimento.ordemDeFornecimento?.observacaoDoGestor,
      dataDeEntrega: ordemFornecimento.ordemDeFornecimento?.dataDeEntrega
        ? moment(ordemFornecimento.ordemDeFornecimento?.dataDeEntrega).format(DATE_TIME_FORMAT)
        : null
    });
  }

  private createFromForm(): IOrdemFornecimento {
    return {
      listaDosArquivos: this.editForm.get(['listaDeArquivo'])!.value,
      ordemDeFornecimento: {
        id: this.ordemFornecimento!.ordemDeFornecimento!.id,
        gestorDaOf: this.editForm.get(['usuarioGestor'])!.value,
        numero: this.editForm.get(['numero'])!.value,
        valorUstibb: this.editForm.get(['ustibb'])!.value,
        observacaoDoGestor: this.editForm.get(['observacoes'])!.value,
        dataDeEntrega: this.editForm.get(['dataDeEntrega'])?.value
          ? moment(this.editForm.get(['dataDeEntrega'])!.value, DATE_TIME_FORMAT)
          : undefined
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
    artefato.complexidade = undefined;
    this.gerenciadorDeOfsService.updateIsTestArquivo(this.ordemFornecimento!.ordemDeFornecimento!, artefato).subscribe(
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
    this.gerenciadorDeOfsService.updateEstadoArquivo(this.ordemFornecimento!.ordemDeFornecimento!, artefatoOrdemDeFornecimento).subscribe(
      () => {},
      () => {
        artefatoOrdemDeFornecimento.estado = estadoArquivoAnterior;
      }
    );
  }

  deletarArquivoDaOf(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): void {
    const modalRef = this.modalService.open(LinhaArtefatoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.artefato = this.getNomeDoArquivo(artefatoOrdemDeFornecimento.artefato!.localDoArtefato!);

    const subscription = this.eventManager.subscribe('linhaArtefatoDelete', () => {
      this.gerenciadorDeOfsService.deletarArquivoDaOf(artefatoOrdemDeFornecimento).subscribe(response => {
        this.ordemFornecimento = response.body!;
        this.updateForm(this.ordemFornecimento);
      });
      this.eventManager.destroy(subscription);
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

  complexidadeIsVisivel(artefato: IArtefato, complexidade: string): boolean {
    if (artefato.artefatoDeTest) return false;

    switch (complexidade) {
      case 'MUITO_BAIXA': {
        return artefato.extensao!.includes('java');
      }
      case 'BAIXA': {
        return (
          artefato.extensao!.includes('css') ||
          artefato.extensao!.includes('scss') ||
          artefato.extensao!.includes('ts') ||
          artefato.extensao!.includes('js') ||
          artefato.extensao!.includes('json') ||
          artefato.extensao!.includes('xml') ||
          artefato.extensao!.includes('yaml') ||
          artefato.extensao!.includes('yml') ||
          artefato.extensao!.includes('java')
        );
      }
      case 'MEDIA': {
        return (
          artefato.extensao!.includes('css') ||
          artefato.extensao!.includes('scss') ||
          artefato.extensao!.includes('ts') ||
          artefato.extensao!.includes('js') ||
          artefato.extensao!.includes('json') ||
          artefato.extensao!.includes('xml') ||
          artefato.extensao!.includes('yaml') ||
          artefato.extensao!.includes('yml') ||
          artefato.extensao!.includes('java')
        );
      }
      case 'ALTA': {
        return (
          artefato.extensao!.includes('css') ||
          artefato.extensao!.includes('scss') ||
          artefato.extensao!.includes('ts') ||
          artefato.extensao!.includes('js') ||
          artefato.extensao!.includes('json') ||
          artefato.extensao!.includes('xml') ||
          artefato.extensao!.includes('yaml') ||
          artefato.extensao!.includes('yml') ||
          artefato.extensao!.includes('java')
        );
      }
      case 'MUITO_ALTA': {
        return artefato.extensao!.includes('java');
      }
    }
    return false;
  }

  salvarValorUstibb(valorUstibb?: string, ordemDeFornecimentoId?: number): void {
    this.gerenciadorDeOfsService.salvarValorUstibb(Number(valorUstibb), ordemDeFornecimentoId).subscribe();
  }

  goToLink(): void {
    const url = `https://genti.intranet.bb.com.br/ccm/web/projects/Fabricas%20de%20Software#action=com.ibm.team.workitem.viewWorkItem&id=${
      this.editForm.get('numero')!.value
    }`;
    window.open(url, '_blank');
  }

  salvarNumeroOf(): void {
    if (this.editForm?.get('numero')?.valid && !!this.ordemFornecimento?.ordemDeFornecimento?.id) {
      this.gerenciadorDeOfsService
        .salvarNumeroOf(this.editForm?.get('numero')?.value, this.ordemFornecimento?.ordemDeFornecimento?.id)
        .subscribe();
    }
  }

  salvarObservacoes(observacoes: string, ordemDeFornecimentoId: number): void {
    this.gerenciadorDeOfsService.salvarObservacoes(observacoes, ordemDeFornecimentoId).subscribe();
  }

  atualizaDataDeEntrega(): void {
    this.editForm.patchValue({
      dataDeEntrega: moment().format(DATE_TIME_FORMAT)
    });
  }
}
