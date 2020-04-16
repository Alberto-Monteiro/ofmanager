import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IArtefato, Artefato } from 'app/shared/model/artefato.model';
import { ArtefatoService } from './artefato.service';

@Component({
  selector: 'of-artefato-update',
  templateUrl: './artefato-update.component.html'
})
export class ArtefatoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    localDoArtefato: [null, [Validators.required]],
    extensao: [null, [Validators.required]],
    complexidade: [],
    artefatoDeTest: [],
    createdDate: []
  });

  constructor(protected artefatoService: ArtefatoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ artefato }) => {
      if (!artefato.id) {
        const today = moment().startOf('day');
        artefato.createdDate = today;
      }

      this.updateForm(artefato);
    });
  }

  updateForm(artefato: IArtefato): void {
    this.editForm.patchValue({
      id: artefato.id,
      localDoArtefato: artefato.localDoArtefato,
      extensao: artefato.extensao,
      complexidade: artefato.complexidade,
      artefatoDeTest: artefato.artefatoDeTest,
      createdDate: artefato.createdDate ? artefato.createdDate.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const artefato = this.createFromForm();
    if (artefato.id !== undefined) {
      this.subscribeToSaveResponse(this.artefatoService.update(artefato));
    } else {
      this.subscribeToSaveResponse(this.artefatoService.create(artefato));
    }
  }

  private createFromForm(): IArtefato {
    return {
      ...new Artefato(),
      id: this.editForm.get(['id'])!.value,
      localDoArtefato: this.editForm.get(['localDoArtefato'])!.value,
      extensao: this.editForm.get(['extensao'])!.value,
      complexidade: this.editForm.get(['complexidade'])!.value,
      artefatoDeTest: this.editForm.get(['artefatoDeTest'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArtefato>>): void {
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
}
