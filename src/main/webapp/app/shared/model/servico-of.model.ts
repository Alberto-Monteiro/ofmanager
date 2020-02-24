import { Moment } from 'moment';
import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { EstadoOf } from 'app/shared/model/enumerations/estado-of.model';
import { IUser } from 'app/core/user/user.model';

export interface IServicoOf {
  id?: number;
  numero?: number;
  estado?: EstadoOf;
  observacaoDoGestor?: any;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  valorUstibb?: number;
  arquivoDaOfs?: IArquivoDaOf[];
  gestorDaOf?: IUser;
  gestorDaOfLogin?: string;
  gestorDaOfId?: number;
  donoDaOf?: IUser;
  donoDaOfLogin?: string;
  donoDaOfId?: number;
}

export class ServicoOf implements IServicoOf {
  constructor(
    public id?: number,
    public numero?: number,
    public estado?: EstadoOf,
    public observacaoDoGestor?: any,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public valorUstibb?: number,
    public arquivoDaOfs?: IArquivoDaOf[],
    public gestorDaOf?: IUser,
    public gestorDaOfLogin?: string,
    public gestorDaOfId?: number,
    public donoDaOf?: IUser,
    public donoDaOfLogin?: string,
    public donoDaOfId?: number
  ) {}
}
