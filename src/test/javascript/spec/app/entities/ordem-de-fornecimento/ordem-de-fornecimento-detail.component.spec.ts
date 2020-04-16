import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { OfmanagerTestModule } from '../../../test.module';
import { OrdemDeFornecimentoDetailComponent } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento-detail.component';
import { OrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';

describe('Component Tests', () => {
  describe('OrdemDeFornecimento Management Detail Component', () => {
    let comp: OrdemDeFornecimentoDetailComponent;
    let fixture: ComponentFixture<OrdemDeFornecimentoDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ ordemDeFornecimento: new OrdemDeFornecimento(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [OrdemDeFornecimentoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(OrdemDeFornecimentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrdemDeFornecimentoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load ordemDeFornecimento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ordemDeFornecimento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
