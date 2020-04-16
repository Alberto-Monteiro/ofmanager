import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { ArtefatoComponent } from './artefato.component';
import { ArtefatoDetailComponent } from './artefato-detail.component';
import { ArtefatoUpdateComponent } from './artefato-update.component';
import { ArtefatoDeleteDialogComponent } from './artefato-delete-dialog.component';
import { artefatoRoute } from './artefato.route';

@NgModule({
  imports: [OfmanagerSharedModule, RouterModule.forChild(artefatoRoute)],
  declarations: [ArtefatoComponent, ArtefatoDetailComponent, ArtefatoUpdateComponent, ArtefatoDeleteDialogComponent],
  entryComponents: [ArtefatoDeleteDialogComponent]
})
export class OfmanagerArtefatoModule {}
