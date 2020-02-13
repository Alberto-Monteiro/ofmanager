import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

export interface IOrdemFornecimento {
  id?: number;
  numero?: number;
  listaDosArquivos?: string;
  arquivoDaOfs?: IArquivoDaOf;
  mapArquivoDaOf?: Map<string, IArquivoDaOf[]>;
}

export class OrdemFornecimento implements IOrdemFornecimento {
  constructor(
    public id?: number,
    public numero?: number,
    public listaDosArquivos?: string,
    public arquivoDaOfs?: IArquivoDaOf,
    public mapArquivoDaOf?: Map<string, IArquivoDaOf[]>
  ) {}
}
