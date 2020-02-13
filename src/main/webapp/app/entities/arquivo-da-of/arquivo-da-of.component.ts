import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ArquivoDaOfService } from './arquivo-da-of.service';
import { ArquivoDaOfDeleteDialogComponent } from './arquivo-da-of-delete-dialog.component';

@Component({
  selector: 'of-arquivo-da-of',
  templateUrl: './arquivo-da-of.component.html'
})
export class ArquivoDaOfComponent implements OnInit, OnDestroy {
  arquivoDaOfs?: IArquivoDaOf[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected arquivoDaOfService: ArquivoDaOfService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.arquivoDaOfService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IArquivoDaOf[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInArquivoDaOfs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IArquivoDaOf): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInArquivoDaOfs(): void {
    this.eventSubscriber = this.eventManager.subscribe('arquivoDaOfListModification', () => this.loadPage());
  }

  delete(arquivoDaOf: IArquivoDaOf): void {
    const modalRef = this.modalService.open(ArquivoDaOfDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.arquivoDaOf = arquivoDaOf;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IArquivoDaOf[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/arquivo-da-of'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.arquivoDaOfs = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
