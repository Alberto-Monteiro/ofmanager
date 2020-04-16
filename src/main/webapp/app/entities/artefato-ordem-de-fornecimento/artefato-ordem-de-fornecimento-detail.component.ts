import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

@Component({
  selector: 'of-artefato-ordem-de-fornecimento-detail',
  templateUrl: './artefato-ordem-de-fornecimento-detail.component.html'
})
export class ArtefatoOrdemDeFornecimentoDetailComponent implements OnInit {
  artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(
      ({ artefatoOrdemDeFornecimento }) => (this.artefatoOrdemDeFornecimento = artefatoOrdemDeFornecimento)
    );
  }

  previousState(): void {
    window.history.back();
  }
}
