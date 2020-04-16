import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IArtefato } from 'app/shared/model/artefato.model';

type EntityResponseType = HttpResponse<IArtefato>;
type EntityArrayResponseType = HttpResponse<IArtefato[]>;

@Injectable({ providedIn: 'root' })
export class ArtefatoService {
  public resourceUrl = SERVER_API_URL + 'api/artefatoes';

  constructor(protected http: HttpClient) {}

  create(artefato: IArtefato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(artefato);
    return this.http
      .post<IArtefato>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(artefato: IArtefato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(artefato);
    return this.http
      .put<IArtefato>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IArtefato>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IArtefato[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(artefato: IArtefato): IArtefato {
    const copy: IArtefato = Object.assign({}, artefato, {
      createdDate: artefato.createdDate && artefato.createdDate.isValid() ? artefato.createdDate.toJSON() : undefined
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
      res.body.forEach((artefato: IArtefato) => {
        artefato.createdDate = artefato.createdDate ? moment(artefato.createdDate) : undefined;
      });
    }
    return res;
  }
}
