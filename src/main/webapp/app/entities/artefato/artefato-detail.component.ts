import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArtefato } from 'app/shared/model/artefato.model';

@Component({
  selector: 'of-artefato-detail',
  templateUrl: './artefato-detail.component.html'
})
export class ArtefatoDetailComponent implements OnInit {
  artefato: IArtefato | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ artefato }) => (this.artefato = artefato));
  }

  previousState(): void {
    window.history.back();
  }
}
