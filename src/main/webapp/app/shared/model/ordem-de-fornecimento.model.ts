import { Moment } from 'moment';
import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';
import { EstadoOrdemDeFornecimento } from 'app/shared/model/enumerations/estado-ordem-de-fornecimento.model';

export interface IOrdemDeFornecimento {
  id?: number;
  numero?: number;
  estado?: EstadoOrdemDeFornecimento;
  observacaoDoGestor?: any;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  valorUstibb?: number;
  artefatoOrdemDeFornecimentos?: IArtefatoOrdemDeFornecimento[];
  gestorDaOfLogin?: string;
  gestorDaOfId?: number;
  donoDaOfLogin?: string;
  donoDaOfId?: number;
}

export class OrdemDeFornecimento implements IOrdemDeFornecimento {
  constructor(
    public id?: number,
    public numero?: number,
    public estado?: EstadoOrdemDeFornecimento,
    public observacaoDoGestor?: any,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public valorUstibb?: number,
    public artefatoOrdemDeFornecimentos?: IArtefatoOrdemDeFornecimento[],
    public gestorDaOfLogin?: string,
    public gestorDaOfId?: number,
    public donoDaOfLogin?: string,
    public donoDaOfId?: number
  ) {}
}
