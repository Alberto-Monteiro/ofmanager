import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  templateUrl: './linha-artefato-delete-dialog.component.html'
})
export class LinhaArtefatoDeleteDialogComponent {
  artefato?: string;

  constructor(public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.eventManager.broadcast('linhaArtefatoDelete');
    this.activeModal.close();
  }
}
