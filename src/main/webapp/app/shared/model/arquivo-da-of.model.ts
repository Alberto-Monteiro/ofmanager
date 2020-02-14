import { EstadoArquivo } from 'app/shared/model/enumerations/estado-arquivo.model';
import { Arquivo } from 'app/shared/model/arquivo.model';

export interface IArquivoDaOf {
  id?: number;
  estadoArquivo?: EstadoArquivo;
  servicoOfId?: number;
  arquivoId?: number;
  arquivo?: Arquivo;
}

export class ArquivoDaOf implements IArquivoDaOf {
  constructor(public id?: number, public estadoArquivo?: EstadoArquivo, public servicoOfId?: number, public arquivoId?: number, public arquivo?: Arquivo) {}
}