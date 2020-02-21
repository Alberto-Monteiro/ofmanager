import { Moment } from 'moment';
import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { IUser } from 'app/core/user/user.model';

export interface IServicoOf {
  id?: number;
  userid?: number;
  gestorDaOf?: IUser;
  donoDaOf?: IUser;
  numero?: number;
  createdDate?: Moment;
  arquivoDaOfs?: IArquivoDaOf[];
  gestorDaOfId?: number;
  donoDaOfId?: number;
}

export class ServicoOf implements IServicoOf {
  constructor(
    public id?: number,
    public userid?: number,
    public gestorDaOf?: IUser,
    public donoDaOf?: IUser,
    public numero?: number,
    public createdDate?: Moment,
    public arquivoDaOfs?: IArquivoDaOf[],
    public gestorDaOfId?: number,
    public donoDaOfId?: number
  ) {}
}
