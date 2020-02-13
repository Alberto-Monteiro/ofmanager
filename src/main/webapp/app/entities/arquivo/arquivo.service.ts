import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IArquivo } from 'app/shared/model/arquivo.model';

type EntityResponseType = HttpResponse<IArquivo>;
type EntityArrayResponseType = HttpResponse<IArquivo[]>;

@Injectable({ providedIn: 'root' })
export class ArquivoService {
  public resourceUrl = SERVER_API_URL + 'api/arquivos';

  constructor(protected http: HttpClient) {}

  create(arquivo: IArquivo): Observable<EntityResponseType> {
    return this.http.post<IArquivo>(this.resourceUrl, arquivo, { observe: 'response' });
  }

  update(arquivo: IArquivo): Observable<EntityResponseType> {
    return this.http.put<IArquivo>(this.resourceUrl, arquivo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArquivo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArquivo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
