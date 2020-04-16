import { Moment } from 'moment';
import { EstadoArtefato } from 'app/shared/model/enumerations/estado-artefato.model';

export interface IArtefatoOrdemDeFornecimento {
  id?: number;
  estado?: EstadoArtefato;
  createdDate?: Moment;
  artefatoId?: number;
  ordemDeFornecimentoId?: number;
}

export class ArtefatoOrdemDeFornecimento implements IArtefatoOrdemDeFornecimento {
  constructor(
    public id?: number,
    public estado?: EstadoArtefato,
    public createdDate?: Moment,
    public artefatoId?: number,
    public ordemDeFornecimentoId?: number
  ) {}
}
