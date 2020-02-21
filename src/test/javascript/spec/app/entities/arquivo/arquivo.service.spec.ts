import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ArquivoService } from 'app/entities/arquivo/arquivo.service';
import { IArquivo, Arquivo } from 'app/shared/model/arquivo.model';
import { Complexidade } from 'app/shared/model/enumerations/complexidade.model';

describe('Service Tests', () => {
  describe('Arquivo Service', () => {
    let injector: TestBed;
    let service: ArquivoService;
    let httpMock: HttpTestingController;
    let elemDefault: IArquivo;
    let expectedResult: IArquivo | IArquivo[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ArquivoService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Arquivo(0, 'AAAAAAA', 'AAAAAAA', Complexidade.MUITO_BAIXA, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Arquivo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Arquivo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Arquivo', () => {
        const returnedFromService = Object.assign(
          {
            caminhoDoArquivo: 'BBBBBB',
            extensao: 'BBBBBB',
            complexidade: 'BBBBBB',
            arquivoDeTest: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Arquivo', () => {
        const returnedFromService = Object.assign(
          {
            caminhoDoArquivo: 'BBBBBB',
            extensao: 'BBBBBB',
            complexidade: 'BBBBBB',
            arquivoDeTest: true
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

      it('should delete a Arquivo', () => {
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
