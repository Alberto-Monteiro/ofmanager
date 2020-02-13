import { EstadoArquivo } from 'app/shared/model/enumerations/estado-arquivo.model';

export interface IArquivoDaOf {
  id?: number;
  estadoArquivo?: EstadoArquivo;
  servicoOfId?: number;
  arquivoId?: number;
}

export class ArquivoDaOf implements IArquivoDaOf {
  constructor(public id?: number, public estadoArquivo?: EstadoArquivo, public servicoOfId?: number, public arquivoId?: number) {}
}
