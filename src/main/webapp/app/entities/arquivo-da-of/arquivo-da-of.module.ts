import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { ArquivoDaOfComponent } from './arquivo-da-of.component';
import { ArquivoDaOfDetailComponent } from './arquivo-da-of-detail.component';
import { ArquivoDaOfUpdateComponent } from './arquivo-da-of-update.component';
import { ArquivoDaOfDeleteDialogComponent } from './arquivo-da-of-delete-dialog.component';
import { arquivoDaOfRoute } from './arquivo-da-of.route';

@NgModule({
  imports: [OfmanagerSharedModule, RouterModule.forChild(arquivoDaOfRoute)],
  declarations: [ArquivoDaOfComponent, ArquivoDaOfDetailComponent, ArquivoDaOfUpdateComponent, ArquivoDaOfDeleteDialogComponent],
  entryComponents: [ArquivoDaOfDeleteDialogComponent]
})
export class OfmanagerArquivoDaOfModule {}
