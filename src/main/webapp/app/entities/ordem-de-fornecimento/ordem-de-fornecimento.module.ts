import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { OrdemDeFornecimentoComponent } from './ordem-de-fornecimento.component';
import { OrdemDeFornecimentoDetailComponent } from './ordem-de-fornecimento-detail.component';
import { OrdemDeFornecimentoUpdateComponent } from './ordem-de-fornecimento-update.component';
import { ordemDeFornecimentoRoute } from './ordem-de-fornecimento.route';

@NgModule({
  imports: [OfmanagerSharedModule, RouterModule.forChild(ordemDeFornecimentoRoute)],
  declarations: [OrdemDeFornecimentoComponent, OrdemDeFornecimentoDetailComponent, OrdemDeFornecimentoUpdateComponent],
  entryComponents: []
})
export class OfmanagerOrdemDeFornecimentoModule {}
