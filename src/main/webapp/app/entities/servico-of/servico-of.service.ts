import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IServicoOf } from 'app/shared/model/servico-of.model';

type EntityResponseType = HttpResponse<IServicoOf>;
type EntityArrayResponseType = HttpResponse<IServicoOf[]>;

@Injectable({ providedIn: 'root' })
export class ServicoOfService {
  public resourceUrl = SERVER_API_URL + 'api/servico-ofs';

  constructor(protected http: HttpClient) {}

  create(servicoOf: IServicoOf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicoOf);
    return this.http
      .post<IServicoOf>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(servicoOf: IServicoOf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicoOf);
    return this.http
      .put<IServicoOf>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IServicoOf>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IServicoOf[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(servicoOf: IServicoOf): IServicoOf {
    const copy: IServicoOf = Object.assign({}, servicoOf, {
      createdDate: servicoOf.createdDate && servicoOf.createdDate.isValid() ? servicoOf.createdDate.toJSON() : undefined
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
      res.body.forEach((servicoOf: IServicoOf) => {
        servicoOf.createdDate = servicoOf.createdDate ? moment(servicoOf.createdDate) : undefined;
      });
    }
    return res;
  }
}
