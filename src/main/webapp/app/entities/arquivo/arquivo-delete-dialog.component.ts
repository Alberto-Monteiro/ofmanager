import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IArquivo } from 'app/shared/model/arquivo.model';
import { ArquivoService } from './arquivo.service';

@Component({
  templateUrl: './arquivo-delete-dialog.component.html'
})
export class ArquivoDeleteDialogComponent {
  arquivo?: IArquivo;

  constructor(protected arquivoService: ArquivoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arquivoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('arquivoListModification');
      this.activeModal.close();
    });
  }
}
