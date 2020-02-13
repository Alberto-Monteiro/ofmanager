import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArquivoDetailComponent } from 'app/entities/arquivo/arquivo-detail.component';
import { Arquivo } from 'app/shared/model/arquivo.model';

describe('Component Tests', () => {
  describe('Arquivo Management Detail Component', () => {
    let comp: ArquivoDetailComponent;
    let fixture: ComponentFixture<ArquivoDetailComponent>;
    const route = ({ data: of({ arquivo: new Arquivo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArquivoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ArquivoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArquivoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load arquivo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.arquivo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
