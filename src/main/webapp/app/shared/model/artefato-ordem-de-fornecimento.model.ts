import { Moment } from 'moment';
import { EstadoArtefato } from 'app/shared/model/enumerations/estado-artefato.model';
import { Artefato, IArtefato } from 'app/shared/model/artefato.model';

export interface IArtefatoOrdemDeFornecimento {
  id?: number;
  estado?: EstadoArtefato;
  createdDate?: Moment;
  artefatoId?: number;
  ordemDeFornecimentoId?: number;
  artefato?: IArtefato;
}

export class ArtefatoOrdemDeFornecimento implements IArtefatoOrdemDeFornecimento {
  constructor(
    public id?: number,
    public estado?: EstadoArtefato,
    public createdDate?: Moment,
    public artefatoId?: number,
    public ordemDeFornecimentoId?: number,
    public artefato?: Artefato
  ) {}
}
