import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'servico-of',
        loadChildren: () => import('./servico-of/servico-of.module').then(m => m.OfmanagerServicoOfModule)
      },
      {
        path: 'arquivo-da-of',
        loadChildren: () => import('./arquivo-da-of/arquivo-da-of.module').then(m => m.OfmanagerArquivoDaOfModule)
      },
      {
        path: 'arquivo',
        loadChildren: () => import('./arquivo/arquivo.module').then(m => m.OfmanagerArquivoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class OfmanagerEntityModule {}
