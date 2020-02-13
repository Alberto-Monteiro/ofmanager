import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

@Component({
  selector: 'of-arquivo-da-of-detail',
  templateUrl: './arquivo-da-of-detail.component.html'
})
export class ArquivoDaOfDetailComponent implements OnInit {
  arquivoDaOf: IArquivoDaOf | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arquivoDaOf }) => (this.arquivoDaOf = arquivoDaOf));
  }

  previousState(): void {
    window.history.back();
  }
}
