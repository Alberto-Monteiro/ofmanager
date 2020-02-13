import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OfmanagerTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { ArquivoDaOfDeleteDialogComponent } from 'app/entities/arquivo-da-of/arquivo-da-of-delete-dialog.component';
import { ArquivoDaOfService } from 'app/entities/arquivo-da-of/arquivo-da-of.service';

describe('Component Tests', () => {
  describe('ArquivoDaOf Management Delete Component', () => {
    let comp: ArquivoDaOfDeleteDialogComponent;
    let fixture: ComponentFixture<ArquivoDaOfDeleteDialogComponent>;
    let service: ArquivoDaOfService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ArquivoDaOfDeleteDialogComponent]
      })
        .overrideTemplate(ArquivoDaOfDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArquivoDaOfDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArquivoDaOfService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
