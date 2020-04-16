import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArtefatoOrdemDeFornecimentoUpdateComponent } from 'app/entities/artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento-update.component';
import { ArtefatoOrdemDeFornecimentoService } from 'app/entities/artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento.service';
import { ArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

describe('Component Tests', () => {
  describe('ArtefatoOrdemDeFornecimento Management Update Component', () => {
    let comp: ArtefatoOrdemDeFornecimentoUpdateComponent;
    let fixture: ComponentFixture<ArtefatoOrdemDeFornecimentoUpdateComponent>;
    let service: ArtefatoOrdemDeFornecimentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArtefatoOrdemDeFornecimentoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ArtefatoOrdemDeFornecimentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArtefatoOrdemDeFornecimentoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArtefatoOrdemDeFornecimentoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ArtefatoOrdemDeFornecimento(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ArtefatoOrdemDeFornecimento();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
