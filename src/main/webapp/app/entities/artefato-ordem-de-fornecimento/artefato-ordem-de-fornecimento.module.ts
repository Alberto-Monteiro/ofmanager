import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { ArtefatoOrdemDeFornecimentoComponent } from './artefato-ordem-de-fornecimento.component';
import { ArtefatoOrdemDeFornecimentoDetailComponent } from './artefato-ordem-de-fornecimento-detail.component';
import { ArtefatoOrdemDeFornecimentoUpdateComponent } from './artefato-ordem-de-fornecimento-update.component';
import { ArtefatoOrdemDeFornecimentoDeleteDialogComponent } from './artefato-ordem-de-fornecimento-delete-dialog.component';
import { artefatoOrdemDeFornecimentoRoute } from './artefato-ordem-de-fornecimento.route';

@NgModule({
  imports: [OfmanagerSharedModule, RouterModule.forChild(artefatoOrdemDeFornecimentoRoute)],
  declarations: [
    ArtefatoOrdemDeFornecimentoComponent,
    ArtefatoOrdemDeFornecimentoDetailComponent,
    ArtefatoOrdemDeFornecimentoUpdateComponent,
    ArtefatoOrdemDeFornecimentoDeleteDialogComponent
  ],
  entryComponents: [ArtefatoOrdemDeFornecimentoDeleteDialogComponent]
})
export class OfmanagerArtefatoOrdemDeFornecimentoModule {}
