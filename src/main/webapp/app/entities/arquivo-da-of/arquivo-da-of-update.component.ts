import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IArquivoDaOf, ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { ArquivoDaOfService } from './arquivo-da-of.service';
import { IServicoOf } from 'app/shared/model/servico-of.model';
import { ServicoOfService } from 'app/entities/servico-of/servico-of.service';
import { IArquivo } from 'app/shared/model/arquivo.model';
import { ArquivoService } from 'app/entities/arquivo/arquivo.service';

type SelectableEntity = IServicoOf | IArquivo;

@Component({
  selector: 'of-arquivo-da-of-update',
  templateUrl: './arquivo-da-of-update.component.html'
})
export class ArquivoDaOfUpdateComponent implements OnInit {
  isSaving = false;
  servicoofs: IServicoOf[] = [];
  arquivos: IArquivo[] = [];

  editForm = this.fb.group({
    id: [],
    estadoArquivo: [],
    servicoOfId: [],
    arquivoId: []
  });

  constructor(
    protected arquivoDaOfService: ArquivoDaOfService,
    protected servicoOfService: ServicoOfService,
    protected arquivoService: ArquivoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arquivoDaOf }) => {
      this.updateForm(arquivoDaOf);

      this.servicoOfService.query().subscribe((res: HttpResponse<IServicoOf[]>) => (this.servicoofs = res.body || []));

      this.arquivoService.query().subscribe((res: HttpResponse<IArquivo[]>) => (this.arquivos = res.body || []));
    });
  }

  updateForm(arquivoDaOf: IArquivoDaOf): void {
    this.editForm.patchValue({
      id: arquivoDaOf.id,
      estadoArquivo: arquivoDaOf.estadoArquivo,
      servicoOfId: arquivoDaOf.servicoOfId,
      arquivoId: arquivoDaOf.arquivoId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arquivoDaOf = this.createFromForm();
    if (arquivoDaOf.id !== undefined) {
      this.subscribeToSaveResponse(this.arquivoDaOfService.update(arquivoDaOf));
    } else {
      this.subscribeToSaveResponse(this.arquivoDaOfService.create(arquivoDaOf));
    }
  }

  private createFromForm(): IArquivoDaOf {
    return {
      ...new ArquivoDaOf(),
      id: this.editForm.get(['id'])!.value,
      estadoArquivo: this.editForm.get(['estadoArquivo'])!.value,
      servicoOfId: this.editForm.get(['servicoOfId'])!.value,
      arquivoId: this.editForm.get(['arquivoId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArquivoDaOf>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
