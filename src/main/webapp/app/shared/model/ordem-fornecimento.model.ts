import {IArquivoDaOf} from 'app/shared/model/arquivo-da-of.model';
import {IServicoOf} from "app/shared/model/servico-of.model";

export interface IOrdemFornecimento {
  listaDosArquivos?: string;
  servicoOf?: IServicoOf;
  mapArquivoDaOf?: Map<string, IArquivoDaOf[]>;
}

export class OrdemFornecimento implements IOrdemFornecimento {
  constructor(
    public listaDosArquivos?: string,
    public servicoOf?: IServicoOf,
    public mapArquivoDaOf?: Map<string, IArquivoDaOf[]>
  ) {
  }
}
