import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

type EntityResponseType = HttpResponse<IArquivoDaOf>;
type EntityArrayResponseType = HttpResponse<IArquivoDaOf[]>;

@Injectable({ providedIn: 'root' })
export class ArquivoDaOfService {
  public resourceUrl = SERVER_API_URL + 'api/arquivo-da-ofs';

  constructor(protected http: HttpClient) {}

  create(arquivoDaOf: IArquivoDaOf): Observable<EntityResponseType> {
    return this.http.post<IArquivoDaOf>(this.resourceUrl, arquivoDaOf, { observe: 'response' });
  }

  update(arquivoDaOf: IArquivoDaOf): Observable<EntityResponseType> {
    return this.http.put<IArquivoDaOf>(this.resourceUrl, arquivoDaOf, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArquivoDaOf>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArquivoDaOf[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
