import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrdemFornecimento } from 'app/shared/model/ordem-fornecimento.model';
import { IUser } from 'app/core/user/user.model';
import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { IArtefato } from 'app/shared/model/artefato.model';
import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

type EntityResponseType = HttpResponse<IOrdemFornecimento>;

@Injectable({ providedIn: 'root' })
export class GerenciadorDeOfsService {
  public resourceUrl = SERVER_API_URL + 'api/gerenciador_de_ofs';

  constructor(protected http: HttpClient) {}

  queryByUser(req?: any): Observable<HttpResponse<IOrdemDeFornecimento[]>> {
    const options = createRequestOption(req);
    return this.http.post<IOrdemDeFornecimento[]>(`${this.resourceUrl}/queryByUser`, req, {
      params: options,
      observe: 'response'
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdemFornecimento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUsuariosGestor(): Observable<HttpResponse<IUser[]>> {
    return this.http.get<IUser[]>(`${this.resourceUrl}/getUsuariosGestor`, { observe: 'response' });
  }

  updateGestorDaOf(ordemFornecimento: IOrdemFornecimento): Observable<EntityResponseType> {
    return this.http.put<IOrdemFornecimento>(`${this.resourceUrl}/updateGestorDaOf`, ordemFornecimento, { observe: 'response' });
  }

  updateEstadoDaOf(servicoOf: IOrdemDeFornecimento): Observable<HttpResponse<IOrdemDeFornecimento>> {
    return this.http.put<IOrdemDeFornecimento>(`${this.resourceUrl}/updateEstadoDaOf`, servicoOf, { observe: 'response' });
  }

  processar(ordemFornecimento: IOrdemFornecimento): Observable<EntityResponseType> {
    return this.http.put<IOrdemFornecimento>(`${this.resourceUrl}/processar`, ordemFornecimento, { observe: 'response' });
  }

  updateIsTestArquivo(ordemDeFornecimento: IOrdemDeFornecimento, artefato: IArtefato): Observable<HttpResponse<IArtefato>> {
    return this.http.put<IArtefato>(`${this.resourceUrl}/updateIsTestArquivo`, artefato, {
      params: new HttpParams().append('ordemDeFornecimentoId', String(ordemDeFornecimento.id)),
      observe: 'response'
    });
  }

  updateComplexidade(ordemDeFornecimento: IOrdemDeFornecimento, artefato: IArtefato): Observable<EntityResponseType> {
    return this.http.put<IOrdemFornecimento>(`${this.resourceUrl}/updateComplexidade`, artefato, {
      params: new HttpParams().append('ordemDeFornecimentoId', String(ordemDeFornecimento.id)),
      observe: 'response'
    });
  }

  updateEstadoArquivo(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): Observable<HttpResponse<IArtefatoOrdemDeFornecimento>> {
    return this.http.put<IArtefatoOrdemDeFornecimento>(`${this.resourceUrl}/updateEstadoArquivo`, artefatoOrdemDeFornecimento, {
      observe: 'response'
    });
  }

  deletarArquivoDaOf(arquivoDaOf: IArtefatoOrdemDeFornecimento): Observable<EntityResponseType> {
    return this.http.delete(`${this.resourceUrl}/deletarArquivoDaOf/${arquivoDaOf.id}`, { observe: 'response' });
  }

  downloadPlanilha(idServicoOf?: number): Observable<any> {
    return this.http.get(`${this.resourceUrl}/downloadPlanilha/${idServicoOf}`, { responseType: 'blob' });
  }

  downloadTxt(idServicoOf?: number): Observable<any> {
    return this.http.get(`${this.resourceUrl}/downloadTxt/${idServicoOf}`, { responseType: 'blob' });
  }

  salvarValorUstibb(valorUstibb?: number, ordemDeFornecimentoId?: number): Observable<EntityResponseType> {
    return this.http.put<IOrdemFornecimento>(`${this.resourceUrl}/updateValorUstibb`, valorUstibb, {
      params: new HttpParams().append('ordemDeFornecimentoId', String(ordemDeFornecimentoId)),
      observe: 'response'
    });
  }
}
