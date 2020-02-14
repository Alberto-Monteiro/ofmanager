import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpHeaders, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {JhiEventManager} from 'ng-jhipster';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {IServicoOf} from 'app/shared/model/servico-of.model';

import {ITEMS_PER_PAGE} from 'app/shared/constants/pagination.constants';
import {GerenciadorDeOfsService} from './gerenciador-de-ofs.service';
import {ServicoOfDeleteDialogComponent} from 'app/entities/servico-of/servico-of-delete-dialog.component';

@Component({
  selector: 'of-gerenciador-de-ofs',
  templateUrl: './gerenciador-de-ofs.component.html'
})
export class GerenciadorDeOfsComponent implements OnInit, OnDestroy {
  servicoOfs?: IServicoOf[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected gerenciadorDeOfsService: GerenciadorDeOfsService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInServicoOfs();
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page ? page : this.page;
    this.gerenciadorDeOfsService
      .queryByUser({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IServicoOf[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  protected onSuccess(data: IServicoOf[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/gerenciador_de_ofs'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.servicoOfs = data ? data : [];
  }

  registerChangeInServicoOfs(): void {
    this.eventSubscriber = this.eventManager.subscribe('servicoOfListModification', () => this.loadPage());
  }

  trackId(index: number, item: IServicoOf): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(servicoOf: IServicoOf): void {
    const modalRef = this.modalService.open(ServicoOfDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.servicoOf = servicoOf;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}