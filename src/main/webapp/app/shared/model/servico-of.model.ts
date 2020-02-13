import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

export interface IServicoOf {
  id?: number;
  userid?: number;
  numero?: number;
  arquivoDaOfs?: IArquivoDaOf[];
}

export class ServicoOf implements IServicoOf {
  constructor(public id?: number, public userid?: number, public numero?: number, public arquivoDaOfs?: IArquivoDaOf[]) {}
}
