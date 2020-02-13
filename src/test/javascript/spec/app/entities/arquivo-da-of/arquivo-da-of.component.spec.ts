import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { OfmanagerTestModule } from '../../../test.module';
import { ArquivoDaOfComponent } from 'app/entities/arquivo-da-of/arquivo-da-of.component';
import { ArquivoDaOfService } from 'app/entities/arquivo-da-of/arquivo-da-of.service';
import { ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

describe('Component Tests', () => {
  describe('ArquivoDaOf Management Component', () => {
    let comp: ArquivoDaOfComponent;
    let fixture: ComponentFixture<ArquivoDaOfComponent>;
    let service: ArquivoDaOfService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArquivoDaOfComponent],
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
        .overrideTemplate(ArquivoDaOfComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArquivoDaOfComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArquivoDaOfService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ArquivoDaOf(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.arquivoDaOfs && comp.arquivoDaOfs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ArquivoDaOf(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.arquivoDaOfs && comp.arquivoDaOfs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
