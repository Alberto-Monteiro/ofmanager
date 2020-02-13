import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArquivoDaOfDetailComponent } from 'app/entities/arquivo-da-of/arquivo-da-of-detail.component';
import { ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

describe('Component Tests', () => {
  describe('ArquivoDaOf Management Detail Component', () => {
    let comp: ArquivoDaOfDetailComponent;
    let fixture: ComponentFixture<ArquivoDaOfDetailComponent>;
    const route = ({ data: of({ arquivoDaOf: new ArquivoDaOf(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArquivoDaOfDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ArquivoDaOfDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArquivoDaOfDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load arquivoDaOf on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.arquivoDaOf).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
