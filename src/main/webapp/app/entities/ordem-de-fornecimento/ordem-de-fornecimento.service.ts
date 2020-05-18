import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';

type EntityResponseType = HttpResponse<IOrdemDeFornecimento>;
type EntityArrayResponseType = HttpResponse<IOrdemDeFornecimento[]>;

@Injectable({ providedIn: 'root' })
export class OrdemDeFornecimentoService {
  public resourceUrl = SERVER_API_URL + 'api/ordem-de-fornecimentos';

  constructor(protected http: HttpClient) {}

  create(ordemDeFornecimento: IOrdemDeFornecimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordemDeFornecimento);
    return this.http
      .post<IOrdemDeFornecimento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ordemDeFornecimento: IOrdemDeFornecimento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ordemDeFornecimento);
    return this.http
      .put<IOrdemDeFornecimento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrdemDeFornecimento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrdemDeFornecimento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ordemDeFornecimento: IOrdemDeFornecimento): IOrdemDeFornecimento {
    const copy: IOrdemDeFornecimento = Object.assign({}, ordemDeFornecimento, {
      createdDate:
        ordemDeFornecimento.createdDate && ordemDeFornecimento.createdDate.isValid() ? ordemDeFornecimento.createdDate.toJSON() : undefined,
      lastModifiedDate:
        ordemDeFornecimento.lastModifiedDate && ordemDeFornecimento.lastModifiedDate.isValid()
          ? ordemDeFornecimento.lastModifiedDate.toJSON()
          : undefined,
      dataDeEntrega:
        ordemDeFornecimento.dataDeEntrega && ordemDeFornecimento.dataDeEntrega.isValid()
          ? ordemDeFornecimento.dataDeEntrega.toJSON()
          : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
      res.body.dataDeEntrega = res.body.dataDeEntrega ? moment(res.body.dataDeEntrega) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ordemDeFornecimento: IOrdemDeFornecimento) => {
        ordemDeFornecimento.createdDate = ordemDeFornecimento.createdDate ? moment(ordemDeFornecimento.createdDate) : undefined;
        ordemDeFornecimento.lastModifiedDate = ordemDeFornecimento.lastModifiedDate
          ? moment(ordemDeFornecimento.lastModifiedDate)
          : undefined;
        ordemDeFornecimento.dataDeEntrega = ordemDeFornecimento.dataDeEntrega ? moment(ordemDeFornecimento.dataDeEntrega) : undefined;
      });
    }
    return res;
  }
}
