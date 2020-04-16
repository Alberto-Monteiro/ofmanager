import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArtefatoOrdemDeFornecimentoDetailComponent } from 'app/entities/artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento-detail.component';
import { ArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

describe('Component Tests', () => {
  describe('ArtefatoOrdemDeFornecimento Management Detail Component', () => {
    let comp: ArtefatoOrdemDeFornecimentoDetailComponent;
    let fixture: ComponentFixture<ArtefatoOrdemDeFornecimentoDetailComponent>;
    const route = ({ data: of({ artefatoOrdemDeFornecimento: new ArtefatoOrdemDeFornecimento(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArtefatoOrdemDeFornecimentoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ArtefatoOrdemDeFornecimentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArtefatoOrdemDeFornecimentoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load artefatoOrdemDeFornecimento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.artefatoOrdemDeFornecimento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
