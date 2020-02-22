import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IServicoOf } from 'app/shared/model/servico-of.model';

@Component({
  selector: 'of-servico-of-detail',
  templateUrl: './servico-of-detail.component.html'
})
export class ServicoOfDetailComponent implements OnInit {
  servicoOf: IServicoOf | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicoOf }) => (this.servicoOf = servicoOf));
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
