import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IServicoOf } from 'app/shared/model/servico-of.model';
import { IOrdemFornecimento } from 'app/shared/model/ordem-fornecimento.model';
import { IArquivo } from 'app/shared/model/arquivo.model';
import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { IUser } from 'app/core/user/user.model';

type EntityResponseType = HttpResponse<IOrdemFornecimento>;

@Injectable({ providedIn: 'root' })
export class GerenciadorDeOfsService {
  public resourceUrl = SERVER_API_URL + 'api/gerenciador_de_ofs';

  constructor(protected http: HttpClient) {}

  queryByUser(req?: any): Observable<HttpResponse<IServicoOf[]>> {
    const options = createRequestOption(req);
    return this.http.get<IServicoOf[]>(`${this.resourceUrl}/queryByUser`, { params: options, observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdemFornecimento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  processar(ordemFornecimento: IOrdemFornecimento): Observable<EntityResponseType> {
    return this.http.put<IOrdemFornecimento>(`${this.resourceUrl}/processar`, ordemFornecimento, { observe: 'response' });
  }

  updateIsTestArquivo(arquivo: IArquivo): Observable<HttpResponse<IArquivo>> {
    return this.http.put<IArquivo>(`${this.resourceUrl}/updateIsTestArquivo`, arquivo, { observe: 'response' });
  }

  updateComplexidade(arquivo: IArquivo): Observable<HttpResponse<IArquivo>> {
    return this.http.put<IArquivo>(`${this.resourceUrl}/updateComplexidade`, arquivo, { observe: 'response' });
  }

  updateEstadoArquivo(arquivoDaOf: IArquivoDaOf): Observable<HttpResponse<IArquivoDaOf>> {
    return this.http.put<IArquivoDaOf>(`${this.resourceUrl}/updateEstadoArquivo`, arquivoDaOf, { observe: 'response' });
  }

  downloadPlanilha(idServicoOf?: number): Observable<any> {
    return this.http.get(`${this.resourceUrl}/downloadPlanilha/${idServicoOf}`, { responseType: 'blob' });
  }

  deletarArquivoDaOf(arquivoDaOf: IArquivoDaOf): Observable<EntityResponseType> {
    return this.http.delete(`${this.resourceUrl}/deletarArquivoDaOf/${arquivoDaOf.id}`, { observe: 'response' });
  }

  getUsuariosGestor(): Observable<HttpResponse<IUser[]>> {
    return this.http.get<IUser[]>(`${this.resourceUrl}/getUsuariosGestor`, { observe: 'response' });
  }
}
