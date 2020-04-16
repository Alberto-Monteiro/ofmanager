import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';

@Component({
  selector: 'of-ordem-de-fornecimento-detail',
  templateUrl: './ordem-de-fornecimento-detail.component.html'
})
export class OrdemDeFornecimentoDetailComponent implements OnInit {
  ordemDeFornecimento: IOrdemDeFornecimento | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordemDeFornecimento }) => (this.ordemDeFornecimento = ordemDeFornecimento));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
