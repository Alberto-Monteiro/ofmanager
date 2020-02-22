import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { OfmanagerTestModule } from '../../../test.module';
import { ServicoOfDetailComponent } from 'app/entities/servico-of/servico-of-detail.component';
import { ServicoOf } from 'app/shared/model/servico-of.model';

describe('Component Tests', () => {
  describe('ServicoOf Management Detail Component', () => {
    let comp: ServicoOfDetailComponent;
    let fixture: ComponentFixture<ServicoOfDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ servicoOf: new ServicoOf(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ServicoOfDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ServicoOfDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServicoOfDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load servicoOf on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.servicoOf).toEqual(jasmine.objectContaining({ id: 123 }));
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
