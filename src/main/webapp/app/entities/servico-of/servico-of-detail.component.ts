import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServicoOf } from 'app/shared/model/servico-of.model';

@Component({
  selector: 'of-servico-of-detail',
  templateUrl: './servico-of-detail.component.html'
})
export class ServicoOfDetailComponent implements OnInit {
  servicoOf: IServicoOf | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicoOf }) => (this.servicoOf = servicoOf));
  }

  previousState(): void {
    window.history.back();
  }
}
