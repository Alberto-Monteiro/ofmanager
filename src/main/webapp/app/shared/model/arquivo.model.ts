import {IArquivoDaOf} from 'app/shared/model/arquivo-da-of.model';
import {Complexidade} from 'app/shared/model/enumerations/complexidade.model';

export interface IArquivo {
  id?: number;
  caminhoDoArquivo?: string;
  extensao?: string;
  complexidade?: Complexidade;
  arquivoDaOfs?: IArquivoDaOf[];
  arquivoDeTest?: boolean;
}

export class Arquivo implements IArquivo {
  constructor(
    public id?: number,
    public caminhoDoArquivo?: string,
    public extensao?: string,
    public complexidade?: Complexidade,
    public arquivoDaOfs?: IArquivoDaOf[],
    public arquivoDeTest?: boolean
  ) {
  }
}
