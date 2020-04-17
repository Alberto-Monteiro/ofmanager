import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { GerenciadorDeOfsComponent } from './gerenciador-de-ofs.component';
import { gerenciadorDeOfsRoute } from './gerenciador-de-ofs.route';
import { GerenciadorDeOfsUpdateComponent } from 'app/gerenciadordeofs/gerenciador-de-ofs-update.component';

@NgModule({
  imports: [OfmanagerSharedModule, RouterModule.forChild(gerenciadorDeOfsRoute)],
  declarations: [GerenciadorDeOfsComponent, GerenciadorDeOfsUpdateComponent, GerenciadorDeOfsUpdateComponent],
  entryComponents: []
})
export class OfmanagerGerenciadorDeOfsModule {}
