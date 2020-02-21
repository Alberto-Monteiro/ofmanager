import { Moment } from 'moment';
import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

export interface IServicoOf {
  id?: number;
  userid?: number;
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
    public numero?: number,
    public createdDate?: Moment,
    public arquivoDaOfs?: IArquivoDaOf[],
    public gestorDaOfId?: number,
    public donoDaOfId?: number
  ) {}
}
