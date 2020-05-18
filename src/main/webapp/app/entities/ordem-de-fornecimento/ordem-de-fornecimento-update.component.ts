import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiEventManager, JhiEventWithContent, JhiFileLoadError } from 'ng-jhipster';

import { IOrdemDeFornecimento, OrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { OrdemDeFornecimentoService } from './ordem-de-fornecimento.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'of-ordem-de-fornecimento-update',
  templateUrl: './ordem-de-fornecimento-update.component.html'
})
export class OrdemDeFornecimentoUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [],
    estado: [],
    observacaoDoGestor: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    valorUstibb: [],
    dataDeEntrega: [],
    gestorDaOfId: [],
    donoDaOfId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected ordemDeFornecimentoService: OrdemDeFornecimentoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordemDeFornecimento }) => {
      if (!ordemDeFornecimento.id) {
        const today = moment().startOf('day');
        ordemDeFornecimento.createdDate = today;
        ordemDeFornecimento.lastModifiedDate = today;
        ordemDeFornecimento.dataDeEntrega = today;
      }

      this.updateForm(ordemDeFornecimento);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(ordemDeFornecimento: IOrdemDeFornecimento): void {
    this.editForm.patchValue({
      id: ordemDeFornecimento.id,
      numero: ordemDeFornecimento.numero,
      estado: ordemDeFornecimento.estado,
      observacaoDoGestor: ordemDeFornecimento.observacaoDoGestor,
      createdBy: ordemDeFornecimento.createdBy,
      createdDate: ordemDeFornecimento.createdDate ? ordemDeFornecimento.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: ordemDeFornecimento.lastModifiedBy,
      lastModifiedDate: ordemDeFornecimento.lastModifiedDate ? ordemDeFornecimento.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      valorUstibb: ordemDeFornecimento.valorUstibb,
      dataDeEntrega: ordemDeFornecimento.dataDeEntrega ? ordemDeFornecimento.dataDeEntrega.format(DATE_TIME_FORMAT) : null,
      gestorDaOfId: ordemDeFornecimento.gestorDaOfId,
      donoDaOfId: ordemDeFornecimento.donoDaOfId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('ofmanagerApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordemDeFornecimento = this.createFromForm();
    if (ordemDeFornecimento.id !== undefined) {
      this.subscribeToSaveResponse(this.ordemDeFornecimentoService.update(ordemDeFornecimento));
    } else {
      this.subscribeToSaveResponse(this.ordemDeFornecimentoService.create(ordemDeFornecimento));
    }
  }

  private createFromForm(): IOrdemDeFornecimento {
    return {
      ...new OrdemDeFornecimento(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      observacaoDoGestor: this.editForm.get(['observacaoDoGestor'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorUstibb: this.editForm.get(['valorUstibb'])!.value,
      dataDeEntrega: this.editForm.get(['dataDeEntrega'])!.value
        ? moment(this.editForm.get(['dataDeEntrega'])!.value, DATE_TIME_FORMAT)
        : undefined,
      gestorDaOfId: this.editForm.get(['gestorDaOfId'])!.value,
      donoDaOfId: this.editForm.get(['donoDaOfId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdemDeFornecimento>>): void {
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
