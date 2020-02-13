import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { ServicoOfComponent } from './servico-of.component';
import { ServicoOfDetailComponent } from './servico-of-detail.component';
import { ServicoOfUpdateComponent } from './servico-of-update.component';
import { servicoOfRoute } from './servico-of.route';

@NgModule({
  imports: [OfmanagerSharedModule, RouterModule.forChild(servicoOfRoute)],
  declarations: [ServicoOfComponent, ServicoOfDetailComponent, ServicoOfUpdateComponent],
  entryComponents: []
})
export class OfmanagerServicoOfModule {}
