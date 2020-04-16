import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { OrdemDeFornecimentoUpdateComponent } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento-update.component';
import { OrdemDeFornecimentoService } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento.service';
import { OrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';

describe('Component Tests', () => {
  describe('OrdemDeFornecimento Management Update Component', () => {
    let comp: OrdemDeFornecimentoUpdateComponent;
    let fixture: ComponentFixture<OrdemDeFornecimentoUpdateComponent>;
    let service: OrdemDeFornecimentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [OrdemDeFornecimentoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(OrdemDeFornecimentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdemDeFornecimentoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrdemDeFornecimentoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new OrdemDeFornecimento(123);
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
        const entity = new OrdemDeFornecimento();
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
