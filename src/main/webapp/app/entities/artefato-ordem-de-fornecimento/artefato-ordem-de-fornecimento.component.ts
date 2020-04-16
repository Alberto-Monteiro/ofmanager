import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ArtefatoOrdemDeFornecimentoService } from './artefato-ordem-de-fornecimento.service';
import { ArtefatoOrdemDeFornecimentoDeleteDialogComponent } from './artefato-ordem-de-fornecimento-delete-dialog.component';

@Component({
  selector: 'of-artefato-ordem-de-fornecimento',
  templateUrl: './artefato-ordem-de-fornecimento.component.html'
})
export class ArtefatoOrdemDeFornecimentoComponent implements OnInit, OnDestroy {
  artefatoOrdemDeFornecimentos?: IArtefatoOrdemDeFornecimento[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected artefatoOrdemDeFornecimentoService: ArtefatoOrdemDeFornecimentoService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.artefatoOrdemDeFornecimentoService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IArtefatoOrdemDeFornecimento[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInArtefatoOrdemDeFornecimentos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IArtefatoOrdemDeFornecimento): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInArtefatoOrdemDeFornecimentos(): void {
    this.eventSubscriber = this.eventManager.subscribe('artefatoOrdemDeFornecimentoListModification', () => this.loadPage());
  }

  delete(artefatoOrdemDeFornecimento: IArtefatoOrdemDeFornecimento): void {
    const modalRef = this.modalService.open(ArtefatoOrdemDeFornecimentoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.artefatoOrdemDeFornecimento = artefatoOrdemDeFornecimento;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IArtefatoOrdemDeFornecimento[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/artefato-ordem-de-fornecimento'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.artefatoOrdemDeFornecimentos = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
