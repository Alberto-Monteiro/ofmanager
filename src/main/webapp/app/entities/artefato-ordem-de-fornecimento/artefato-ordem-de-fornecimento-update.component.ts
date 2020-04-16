import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IArtefatoOrdemDeFornecimento, ArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';
import { ArtefatoOrdemDeFornecimentoService } from './artefato-ordem-de-fornecimento.service';
import { IArtefato } from 'app/shared/model/artefato.model';
import { ArtefatoService } from 'app/entities/artefato/artefato.service';
import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { OrdemDeFornecimentoService } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento.service';

type SelectableEntity = IArtefato | IOrdemDeFornecimento;

@Component({
  selector: 'of-artefato-ordem-de-fornecimento-update',
  templateUrl: './artefato-ordem-de-fornecimento-update.component.html'
})
export class ArtefatoOrdemDeFornecimentoUpdateComponent implements OnInit {
  isSaving = false;
  artefatoes: IArtefato[] = [];
  ordemdefornecimentos: IOrdemDeFornecimento[] = [];

  editForm = this.fb.group({
    id: [],
    estado: [],
    createdDate: [],
    artefatoId: [],
    ordemDeFornecimentoId: []
  });

  constructor(
    protected artefatoOrdemDeFornecimentoService: ArtefatoOrdemDeFornecimentoService,
    protected artefatoService: ArtefatoService,
    protected ordemDeFornecimentoService: OrdemDeFornecimentoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ artefatoOrdemDeFornecimento }) => {
      if (!artefatoOrdemDeFornecimento.id) {
        const today = moment().startOf('day');
        artefatoOrdemDeFornecimento.createdDate = today;
      }

      this.updateForm(artefatoOrdemDeFornecimento);

      this.artefatoService.query().subscribe((res: HttpResponse<IArtefato[]>) => (this.artefatoes = res.body || []));

      this.ordemDeFornecimentoService
        .query()
        .subscribe((res: HttpResponse<IOrdemDeFornecimento[]>) => (this.ordemdefornecimentos = res.body || []));
    });
  }

  updateForm(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): void {
    this.editForm.patchValue({
      id: artefatoOrdemDeFornecimento.id,
      estado: artefatoOrdemDeFornecimento.estado,
      createdDate: artefatoOrdemDeFornecimento.createdDate ? artefatoOrdemDeFornecimento.createdDate.format(DATE_TIME_FORMAT) : null,
      artefatoId: artefatoOrdemDeFornecimento.artefatoId,
      ordemDeFornecimentoId: artefatoOrdemDeFornecimento.ordemDeFornecimentoId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const artefatoOrdemDeFornecimento = this.createFromForm();
    if (artefatoOrdemDeFornecimento.id !== undefined) {
      this.subscribeToSaveResponse(this.artefatoOrdemDeFornecimentoService.update(artefatoOrdemDeFornecimento));
    } else {
      this.subscribeToSaveResponse(this.artefatoOrdemDeFornecimentoService.create(artefatoOrdemDeFornecimento));
    }
  }

  private createFromForm(): IArtefatoOrdemDeFornecimento {
    return {
      ...new ArtefatoOrdemDeFornecimento(),
      id: this.editForm.get(['id'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      artefatoId: this.editForm.get(['artefatoId'])!.value,
      ordemDeFornecimentoId: this.editForm.get(['ordemDeFornecimentoId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArtefatoOrdemDeFornecimento>>): void {
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
