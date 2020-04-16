import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ArtefatoService } from 'app/entities/artefato/artefato.service';
import { IArtefato, Artefato } from 'app/shared/model/artefato.model';
import { ComplexidadeArtefato } from 'app/shared/model/enumerations/complexidade-artefato.model';

describe('Service Tests', () => {
  describe('Artefato Service', () => {
    let injector: TestBed;
    let service: ArtefatoService;
    let httpMock: HttpTestingController;
    let elemDefault: IArtefato;
    let expectedResult: IArtefato | IArtefato[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ArtefatoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Artefato(0, 'AAAAAAA', 'AAAAAAA', ComplexidadeArtefato.MUITO_BAIXA, false, currentDate);
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

      it('should create a Artefato', () => {
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

        service.create(new Artefato()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Artefato', () => {
        const returnedFromService = Object.assign(
          {
            localDoArtefato: 'BBBBBB',
            extensao: 'BBBBBB',
            complexidade: 'BBBBBB',
            artefatoDeTest: true,
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

      it('should return a list of Artefato', () => {
        const returnedFromService = Object.assign(
          {
            localDoArtefato: 'BBBBBB',
            extensao: 'BBBBBB',
            complexidade: 'BBBBBB',
            artefatoDeTest: true,
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

      it('should delete a Artefato', () => {
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
