import {IArquivoDaOf} from 'app/shared/model/arquivo-da-of.model';
import {Moment} from 'moment';

export interface IServicoOf {
  id?: number;
  userid?: number;
  userName?: string;
  numero?: number;
  createdDate?: Moment;
  arquivoDaOfs?: IArquivoDaOf[];
}

export class ServicoOf implements IServicoOf {
  constructor(
    public id?: number,
    public userid?: number,
    public userName?: string,
    public numero?: number,
    public createdDate?: Moment,
    public arquivoDaOfs?: IArquivoDaOf[]
  ) {
  }
}
