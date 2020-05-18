import { Moment } from 'moment';
import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';
import { EstadoOrdemDeFornecimento } from 'app/shared/model/enumerations/estado-ordem-de-fornecimento.model';
import { IUser } from 'app/core/user/user.model';

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
  dataDeEntrega?: Moment;
  artefatoOrdemDeFornecimentos?: IArtefatoOrdemDeFornecimento[];
  gestorDaOfLogin?: string;
  gestorDaOfId?: number;
  donoDaOfLogin?: string;
  donoDaOfId?: number;
  donoDaOfFirstName?: string;
  donoDaOfLastName?: string;
  gestorDaOfFirstName?: string;
  gestorDaOfLastName?: string;
  gestorDaOf?: IUser;
  donoDaOf?: IUser;
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
    public dataDeEntrega?: Moment,
    public artefatoOrdemDeFornecimentos?: IArtefatoOrdemDeFornecimento[],
    public gestorDaOfLogin?: string,
    public gestorDaOfId?: number,
    public donoDaOfLogin?: string,
    public donoDaOfId?: number,
    public donoDaOfFirstName?: string,
    public donoDaOfLastName?: string,
    public gestorDaOfFirstName?: string,
    public gestorDaOfLastName?: string,
    public gestorDaOf?: IUser,
    public donoDaOf?: IUser
  ) {}
}
