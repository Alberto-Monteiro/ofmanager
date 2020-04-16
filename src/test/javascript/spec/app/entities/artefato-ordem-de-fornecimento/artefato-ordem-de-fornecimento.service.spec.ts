import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ArtefatoOrdemDeFornecimentoService } from 'app/entities/artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento.service';
import { IArtefatoOrdemDeFornecimento, ArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';
import { EstadoArtefato } from 'app/shared/model/enumerations/estado-artefato.model';

describe('Service Tests', () => {
  describe('ArtefatoOrdemDeFornecimento Service', () => {
    let injector: TestBed;
    let service: ArtefatoOrdemDeFornecimentoService;
    let httpMock: HttpTestingController;
    let elemDefault: IArtefatoOrdemDeFornecimento;
    let expectedResult: IArtefatoOrdemDeFornecimento | IArtefatoOrdemDeFornecimento[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ArtefatoOrdemDeFornecimentoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ArtefatoOrdemDeFornecimento(0, EstadoArtefato.CRIANDO, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ArtefatoOrdemDeFornecimento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate
          },
          returnedFromService
        );

        service.create(new ArtefatoOrdemDeFornecimento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ArtefatoOrdemDeFornecimento', () => {
        const returnedFromService = Object.assign(
          {
            estado: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ArtefatoOrdemDeFornecimento', () => {
        const returnedFromService = Object.assign(
          {
            estado: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ArtefatoOrdemDeFornecimento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
