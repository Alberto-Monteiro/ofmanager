import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
    return this.http.post<IServicoOf>(this.resourceUrl, servicoOf, { observe: 'response' });
  }

  update(servicoOf: IServicoOf): Observable<EntityResponseType> {
    return this.http.put<IServicoOf>(this.resourceUrl, servicoOf, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServicoOf>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServicoOf[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
