import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArtefatoUpdateComponent } from 'app/entities/artefato/artefato-update.component';
import { ArtefatoService } from 'app/entities/artefato/artefato.service';
import { Artefato } from 'app/shared/model/artefato.model';

describe('Component Tests', () => {
  describe('Artefato Management Update Component', () => {
    let comp: ArtefatoUpdateComponent;
    let fixture: ComponentFixture<ArtefatoUpdateComponent>;
    let service: ArtefatoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArtefatoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ArtefatoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArtefatoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArtefatoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Artefato(123);
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
        const entity = new Artefato();
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
