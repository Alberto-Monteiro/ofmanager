import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IServicoOf } from 'app/shared/model/servico-of.model';
import { ServicoOfService } from './servico-of.service';

@Component({
  templateUrl: './servico-of-delete-dialog.component.html'
})
export class ServicoOfDeleteDialogComponent {
  servicoOf?: IServicoOf;

  constructor(protected servicoOfService: ServicoOfService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.servicoOfService.delete(id).subscribe(() => {
      this.eventManager.broadcast('servicoOfListModification');
      this.activeModal.close();
    });
  }
}
