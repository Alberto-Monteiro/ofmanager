import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IServicoOf, ServicoOf } from 'app/shared/model/servico-of.model';
import { ServicoOfService } from './servico-of.service';

@Component({
  selector: 'of-servico-of-update',
  templateUrl: './servico-of-update.component.html'
})
export class ServicoOfUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    userid: [null, [Validators.required]],
    numero: [null, [Validators.required]]
  });

  constructor(protected servicoOfService: ServicoOfService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicoOf }) => {
      this.updateForm(servicoOf);
    });
  }

  updateForm(servicoOf: IServicoOf): void {
    this.editForm.patchValue({
      id: servicoOf.id,
      userid: servicoOf.userid,
      numero: servicoOf.numero
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const servicoOf = this.createFromForm();
    if (servicoOf.id !== undefined) {
      this.subscribeToSaveResponse(this.servicoOfService.update(servicoOf));
    } else {
      this.subscribeToSaveResponse(this.servicoOfService.create(servicoOf));
    }
  }

  private createFromForm(): IServicoOf {
    return {
      ...new ServicoOf(),
      id: this.editForm.get(['id'])!.value,
      userid: this.editForm.get(['userid'])!.value,
      numero: this.editForm.get(['numero'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServicoOf>>): void {
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
