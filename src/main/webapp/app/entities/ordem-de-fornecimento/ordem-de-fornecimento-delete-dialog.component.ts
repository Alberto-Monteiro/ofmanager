import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { OrdemDeFornecimentoService } from './ordem-de-fornecimento.service';

@Component({
  templateUrl: './ordem-de-fornecimento-delete-dialog.component.html'
})
export class OrdemDeFornecimentoDeleteDialogComponent {
  ordemDeFornecimento?: IOrdemDeFornecimento;

  constructor(
    protected ordemDeFornecimentoService: OrdemDeFornecimentoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordemDeFornecimentoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ordemDeFornecimentoListModification');
      this.activeModal.close();
    });
  }
}
