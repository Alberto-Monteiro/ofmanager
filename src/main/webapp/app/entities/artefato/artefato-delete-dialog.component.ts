import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IArtefato } from 'app/shared/model/artefato.model';
import { ArtefatoService } from './artefato.service';

@Component({
  templateUrl: './artefato-delete-dialog.component.html'
})
export class ArtefatoDeleteDialogComponent {
  artefato?: IArtefato;

  constructor(protected artefatoService: ArtefatoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.artefatoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('artefatoListModification');
      this.activeModal.close();
    });
  }
}
