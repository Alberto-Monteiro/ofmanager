import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { OfmanagerTestModule } from '../../../test.module';
import { OrdemDeFornecimentoComponent } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento.component';
import { OrdemDeFornecimentoService } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento.service';
import { OrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';

describe('Component Tests', () => {
  describe('OrdemDeFornecimento Management Component', () => {
    let comp: OrdemDeFornecimentoComponent;
    let fixture: ComponentFixture<OrdemDeFornecimentoComponent>;
    let service: OrdemDeFornecimentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [OrdemDeFornecimentoComponent],
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
        .overrideTemplate(OrdemDeFornecimentoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdemDeFornecimentoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrdemDeFornecimentoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new OrdemDeFornecimento(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ordemDeFornecimentos && comp.ordemDeFornecimentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new OrdemDeFornecimento(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ordemDeFornecimentos && comp.ordemDeFornecimentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
