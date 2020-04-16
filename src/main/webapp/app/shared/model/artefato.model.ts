import { Moment } from 'moment';
import { ComplexidadeArtefato } from 'app/shared/model/enumerations/complexidade-artefato.model';

export interface IArtefato {
  id?: number;
  localDoArtefato?: string;
  extensao?: string;
  complexidade?: ComplexidadeArtefato;
  artefatoDeTest?: boolean;
  createdDate?: Moment;
}

export class Artefato implements IArtefato {
  constructor(
    public id?: number,
    public localDoArtefato?: string,
    public extensao?: string,
    public complexidade?: ComplexidadeArtefato,
    public artefatoDeTest?: boolean,
    public createdDate?: Moment
  ) {
    this.artefatoDeTest = this.artefatoDeTest || false;
  }
}
