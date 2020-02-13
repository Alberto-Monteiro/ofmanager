import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ServicoOfUpdateComponent } from 'app/entities/servico-of/servico-of-update.component';
import { ServicoOfService } from 'app/entities/servico-of/servico-of.service';
import { ServicoOf } from 'app/shared/model/servico-of.model';

describe('Component Tests', () => {
  describe('ServicoOf Management Update Component', () => {
    let comp: ServicoOfUpdateComponent;
    let fixture: ComponentFixture<ServicoOfUpdateComponent>;
    let service: ServicoOfService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ServicoOfUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ServicoOfUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServicoOfUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServicoOfService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ServicoOf(123);
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
        const entity = new ServicoOf();
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
