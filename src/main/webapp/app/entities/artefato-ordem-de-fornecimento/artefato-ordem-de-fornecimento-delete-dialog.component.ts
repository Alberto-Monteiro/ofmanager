import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';
import { ArtefatoOrdemDeFornecimentoService } from './artefato-ordem-de-fornecimento.service';

@Component({
  templateUrl: './artefato-ordem-de-fornecimento-delete-dialog.component.html'
})
export class ArtefatoOrdemDeFornecimentoDeleteDialogComponent {
  artefatoOrdemDeFornecimento?: IArtefatoOrdemDeFornecimento;

  constructor(
    protected artefatoOrdemDeFornecimentoService: ArtefatoOrdemDeFornecimentoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.artefatoOrdemDeFornecimentoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('artefatoOrdemDeFornecimentoListModification');
      this.activeModal.close();
    });
  }
}
