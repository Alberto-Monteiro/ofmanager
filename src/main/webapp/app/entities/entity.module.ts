import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ordem-de-fornecimento',
        loadChildren: () => import('./ordem-de-fornecimento/ordem-de-fornecimento.module').then(m => m.OfmanagerOrdemDeFornecimentoModule)
      },
      {
        path: 'artefato-ordem-de-fornecimento',
        loadChildren: () =>
          import('./artefato-ordem-de-fornecimento/artefato-ordem-de-fornecimento.module').then(
            m => m.OfmanagerArtefatoOrdemDeFornecimentoModule
          )
      },
      {
        path: 'artefato',
        loadChildren: () => import('./artefato/artefato.module').then(m => m.OfmanagerArtefatoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class OfmanagerEntityModule {}
