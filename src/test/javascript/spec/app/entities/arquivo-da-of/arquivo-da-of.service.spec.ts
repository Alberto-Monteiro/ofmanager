import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ArquivoDaOfService } from 'app/entities/arquivo-da-of/arquivo-da-of.service';
import { IArquivoDaOf, ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { EstadoArquivo } from 'app/shared/model/enumerations/estado-arquivo.model';

describe('Service Tests', () => {
  describe('ArquivoDaOf Service', () => {
    let injector: TestBed;
    let service: ArquivoDaOfService;
    let httpMock: HttpTestingController;
    let elemDefault: IArquivoDaOf;
    let expectedResult: IArquivoDaOf | IArquivoDaOf[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ArquivoDaOfService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ArquivoDaOf(0, EstadoArquivo.CRIANDO);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ArquivoDaOf', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ArquivoDaOf()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ArquivoDaOf', () => {
        const returnedFromService = Object.assign(
          {
            estadoArquivo: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ArquivoDaOf', () => {
        const returnedFromService = Object.assign(
          {
            estadoArquivo: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ArquivoDaOf', () => {
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
