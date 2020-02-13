import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArquivoDaOfUpdateComponent } from 'app/entities/arquivo-da-of/arquivo-da-of-update.component';
import { ArquivoDaOfService } from 'app/entities/arquivo-da-of/arquivo-da-of.service';
import { ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

describe('Component Tests', () => {
  describe('ArquivoDaOf Management Update Component', () => {
    let comp: ArquivoDaOfUpdateComponent;
    let fixture: ComponentFixture<ArquivoDaOfUpdateComponent>;
    let service: ArquivoDaOfService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArquivoDaOfUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ArquivoDaOfUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArquivoDaOfUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArquivoDaOfService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ArquivoDaOf(123);
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
        const entity = new ArquivoDaOf();
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
