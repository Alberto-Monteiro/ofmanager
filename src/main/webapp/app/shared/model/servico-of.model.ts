import {IArquivoDaOf} from 'app/shared/model/arquivo-da-of.model';
import {Moment} from 'moment';
import {IUser} from "app/core/user/user.model";

export interface IServicoOf {
  id?: number;
  gestorDaOf?: IUser;
  donoDaOf?: IUser;
  numero?: number;
  createdDate?: Moment;
  arquivoDaOfs?: IArquivoDaOf[];
}

export class ServicoOf implements IServicoOf {
  constructor(
    public id?: number,
    public gestorDaOf?: IUser,
    public donoDaOf?: IUser,
    public numero?: number,
    public createdDate?: Moment,
    public arquivoDaOfs?: IArquivoDaOf[]
  ) {
  }
}
