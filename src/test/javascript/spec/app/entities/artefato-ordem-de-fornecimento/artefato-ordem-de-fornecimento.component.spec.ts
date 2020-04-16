import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { OfmanagerTestModule } from '../../../test.module';
import { ArtefatoOrdemDeFornecimentoComponent } from 'app/entities/artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento.component';
import { ArtefatoOrdemDeFornecimentoService } from 'app/entities/artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento.service';
import { ArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

describe('Component Tests', () => {
  describe('ArtefatoOrdemDeFornecimento Management Component', () => {
    let comp: ArtefatoOrdemDeFornecimentoComponent;
    let fixture: ComponentFixture<ArtefatoOrdemDeFornecimentoComponent>;
    let service: ArtefatoOrdemDeFornecimentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArtefatoOrdemDeFornecimentoComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(ArtefatoOrdemDeFornecimentoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArtefatoOrdemDeFornecimentoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArtefatoOrdemDeFornecimentoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ArtefatoOrdemDeFornecimento(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.artefatoOrdemDeFornecimentos && comp.artefatoOrdemDeFornecimentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ArtefatoOrdemDeFornecimento(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.artefatoOrdemDeFornecimentos && comp.artefatoOrdemDeFornecimentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
