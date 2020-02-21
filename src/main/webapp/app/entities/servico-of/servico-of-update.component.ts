import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IServicoOf, ServicoOf } from 'app/shared/model/servico-of.model';
import { ServicoOfService } from './servico-of.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'of-servico-of-update',
  templateUrl: './servico-of-update.component.html'
})
export class ServicoOfUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    userid: [null, [Validators.required]],
    numero: [null, [Validators.required]],
    createdDate: [],
    gestorDaOfId: [],
    donoDaOfId: []
  });

  constructor(
    protected servicoOfService: ServicoOfService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicoOf }) => {
      if (!servicoOf.id) {
        const today = moment().startOf('day');
        servicoOf.createdDate = today;
      }

      this.updateForm(servicoOf);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(servicoOf: IServicoOf): void {
    this.editForm.patchValue({
      id: servicoOf.id,
      userid: servicoOf.userid,
      numero: servicoOf.numero,
      createdDate: servicoOf.createdDate ? servicoOf.createdDate.format(DATE_TIME_FORMAT) : null,
      gestorDaOfId: servicoOf.gestorDaOfId,
      donoDaOfId: servicoOf.donoDaOfId
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
      numero: this.editForm.get(['numero'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      gestorDaOfId: this.editForm.get(['gestorDaOfId'])!.value,
      donoDaOfId: this.editForm.get(['donoDaOfId'])!.value
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
