import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ArquivoUpdateComponent } from 'app/entities/arquivo/arquivo-update.component';
import { ArquivoService } from 'app/entities/arquivo/arquivo.service';
import { Arquivo } from 'app/shared/model/arquivo.model';

describe('Component Tests', () => {
  describe('Arquivo Management Update Component', () => {
    let comp: ArquivoUpdateComponent;
    let fixture: ComponentFixture<ArquivoUpdateComponent>;
    let service: ArquivoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArquivoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ArquivoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArquivoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArquivoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Arquivo(123);
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
        const entity = new Arquivo();
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
