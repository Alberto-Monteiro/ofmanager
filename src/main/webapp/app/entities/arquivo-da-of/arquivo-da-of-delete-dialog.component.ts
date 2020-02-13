import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { ArquivoDaOfService } from './arquivo-da-of.service';

@Component({
  templateUrl: './arquivo-da-of-delete-dialog.component.html'
})
export class ArquivoDaOfDeleteDialogComponent {
  arquivoDaOf?: IArquivoDaOf;

  constructor(
    protected arquivoDaOfService: ArquivoDaOfService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arquivoDaOfService.delete(id).subscribe(() => {
      this.eventManager.broadcast('arquivoDaOfListModification');
      this.activeModal.close();
    });
  }
}
