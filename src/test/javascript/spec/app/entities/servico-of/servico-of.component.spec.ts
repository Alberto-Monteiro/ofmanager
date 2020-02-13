import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { OfmanagerTestModule } from '../../../test.module';
import { ServicoOfComponent } from 'app/entities/servico-of/servico-of.component';
import { ServicoOfService } from 'app/entities/servico-of/servico-of.service';
import { ServicoOf } from 'app/shared/model/servico-of.model';

describe('Component Tests', () => {
  describe('ServicoOf Management Component', () => {
    let comp: ServicoOfComponent;
    let fixture: ComponentFixture<ServicoOfComponent>;
    let service: ServicoOfService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ServicoOfComponent],
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
        .overrideTemplate(ServicoOfComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServicoOfComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServicoOfService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ServicoOf(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.servicoOfs && comp.servicoOfs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ServicoOf(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.servicoOfs && comp.servicoOfs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
