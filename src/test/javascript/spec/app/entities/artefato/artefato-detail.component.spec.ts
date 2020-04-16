import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArtefatoDetailComponent } from 'app/entities/artefato/artefato-detail.component';
import { Artefato } from 'app/shared/model/artefato.model';

describe('Component Tests', () => {
  describe('Artefato Management Detail Component', () => {
    let comp: ArtefatoDetailComponent;
    let fixture: ComponentFixture<ArtefatoDetailComponent>;
    const route = ({ data: of({ artefato: new Artefato(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArtefatoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ArtefatoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArtefatoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load artefato on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.artefato).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
