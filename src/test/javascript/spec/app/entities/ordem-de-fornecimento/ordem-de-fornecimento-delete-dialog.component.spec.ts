import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OfmanagerTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { OrdemDeFornecimentoDeleteDialogComponent } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento-delete-dialog.component';
import { OrdemDeFornecimentoService } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento.service';

describe('Component Tests', () => {
  describe('OrdemDeFornecimento Management Delete Component', () => {
    let comp: OrdemDeFornecimentoDeleteDialogComponent;
    let fixture: ComponentFixture<OrdemDeFornecimentoDeleteDialogComponent>;
    let service: OrdemDeFornecimentoService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [OrdemDeFornecimentoDeleteDialogComponent]
      })
        .overrideTemplate(OrdemDeFornecimentoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrdemDeFornecimentoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OrdemDeFornecimentoService);
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
