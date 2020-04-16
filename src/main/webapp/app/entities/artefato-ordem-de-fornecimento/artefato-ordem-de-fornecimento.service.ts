import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

type EntityResponseType = HttpResponse<IArtefatoOrdemDeFornecimento>;
type EntityArrayResponseType = HttpResponse<IArtefatoOrdemDeFornecimento[]>;

@Injectable({ providedIn: 'root' })
export class ArtefatoOrdemDeFornecimentoService {
  public resourceUrl = SERVER_API_URL + 'api/artefato-ordem-de-fornecimentos';

  constructor(protected http: HttpClient) {}

  create(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(artefatoOrdemDeFornecimento);
    return this.http
      .post<IArtefatoOrdemDeFornecimento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(artefatoOrdemDeFornecimento);
    return this.http
      .put<IArtefatoOrdemDeFornecimento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IArtefatoOrdemDeFornecimento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IArtefatoOrdemDeFornecimento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): IArtefatoOrdemDeFornecimento {
    const copy: IArtefatoOrdemDeFornecimento = Object.assign({}, artefatoOrdemDeFornecimento, {
      createdDate:
        artefatoOrdemDeFornecimento.createdDate && artefatoOrdemDeFornecimento.createdDate.isValid()
          ? artefatoOrdemDeFornecimento.createdDate.toJSON()
          : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento) => {
        artefatoOrdemDeFornecimento.createdDate = artefatoOrdemDeFornecimento.createdDate
          ? moment(artefatoOrdemDeFornecimento.createdDate)
          : undefined;
      });
    }
    return res;
  }
}
