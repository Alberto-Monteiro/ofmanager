import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GerenciadorDeOfsService } from './gerenciador-de-ofs.service';
import { IOrdemFornecimento, OrdemFornecimento } from 'app/shared/model/ordem-fornecimento.model';
import { Arquivo } from 'app/shared/model/arquivo.model';
import { ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import * as fileSaver from 'file-saver';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'of-gerenciador-de-ofs-update',
  templateUrl: './gerenciador-de-ofs-update.component.html'
})
export class GerenciadorDeOfsUpdateComponent implements OnInit {
  isSaving = false;
  ordemFornecimento?: IOrdemFornecimento;

  editForm = this.fb.group({
    id: [null],
    numero: [null, [Validators.required, Validators.max(999999999), Validators.min(100)]],
    listaDeArquivo: [null, [Validators.required]]
  });

  constructor(
    protected gerenciadorDeOfsService: GerenciadorDeOfsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordemFornecimento }) => {
      this.ordemFornecimento = ordemFornecimento;
      this.updateForm(ordemFornecimento);
    });
  }

  updateForm(ordemFornecimento: IOrdemFornecimento): void {
    this.editForm.patchValue({
      id: ordemFornecimento.id,
      numero: ordemFornecimento.numero,
      listaDeArquivo: ordemFornecimento.listaDosArquivos
    });
  }

  private createFromForm(): IOrdemFornecimento {
    return {
      ...new OrdemFornecimento(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      listaDosArquivos: this.editForm.get(['listaDeArquivo'])!.value
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

  atualizaComplexidade(arquivo: Arquivo, complexidade: any): void {
    const complexidadeAnterior = arquivo.complexidade;
    arquivo.complexidade = complexidade;
    this.gerenciadorDeOfsService.updateComplexidade(arquivo).subscribe(
      () => {
        this.toastrService.success('A complexidade foi atualizada', 'OK');
      },
      () => {
        arquivo.complexidade = complexidadeAnterior;
      }
    );
  }

  atualizaEstadoArquivo(arquivoDaOf: ArquivoDaOf, estadoArquivo: any): void {
    const estadoArquivoAnterior = arquivoDaOf.estadoArquivo;
    arquivoDaOf.estadoArquivo = estadoArquivo;
    this.gerenciadorDeOfsService.updateEstadoArquivo(arquivoDaOf).subscribe(
      () => {
        this.toastrService.success('O estado do arquivo foi atualizada', 'OK');
      },
      () => {
        arquivoDaOf.estadoArquivo = estadoArquivoAnterior;
      }
    );
  }

  download(): void {
    this.gerenciadorDeOfsService.downloadPlanilha(this.ordemFornecimento!.id).subscribe(response => {
      const blob: any = new Blob([response], { type: 'text/xlsx' });
      fileSaver.saveAs(blob, `OF-${this.ordemFornecimento!.numero}.xlsx`);
    });
  }
}
