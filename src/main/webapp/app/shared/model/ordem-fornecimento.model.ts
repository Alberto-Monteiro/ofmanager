import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

export interface IOrdemFornecimento {
  listaDosArquivos?: string;
  ordemDeFornecimento?: IOrdemDeFornecimento;
  mapArtefatoOrdemDeFornecimento?: { [key: string]: IArtefatoOrdemDeFornecimento[] };
}

export class OrdemFornecimento implements IOrdemFornecimento {
  constructor(
    public listaDosArquivos?: string,
    public ordemDeFornecimento?: IOrdemDeFornecimento,
    public mapArtefatoOrdemDeFornecimento?: { [key: string]: IArtefatoOrdemDeFornecimento[] }
  ) {}
}
