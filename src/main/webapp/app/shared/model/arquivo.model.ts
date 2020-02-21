import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { Complexidade } from 'app/shared/model/enumerations/complexidade.model';

export interface IArquivo {
  id?: number;
  caminhoDoArquivo?: string;
  extensao?: string;
  complexidade?: Complexidade;
  arquivoDeTest?: boolean;
  arquivoDaOfs?: IArquivoDaOf[];
}

export class Arquivo implements IArquivo {
  constructor(
    public id?: number,
    public caminhoDoArquivo?: string,
    public extensao?: string,
    public complexidade?: Complexidade,
    public arquivoDeTest?: boolean,
    public arquivoDaOfs?: IArquivoDaOf[]
  ) {
    this.arquivoDeTest = this.arquivoDeTest || false;
  }
}
