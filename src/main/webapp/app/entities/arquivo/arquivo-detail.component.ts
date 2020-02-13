import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArquivo } from 'app/shared/model/arquivo.model';

@Component({
  selector: 'of-arquivo-detail',
  templateUrl: './arquivo-detail.component.html'
})
export class ArquivoDetailComponent implements OnInit {
  arquivo: IArquivo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arquivo }) => (this.arquivo = arquivo));
  }

  previousState(): void {
    window.history.back();
  }
}
