import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GerenciadorDeOfsService } from './gerenciador-de-ofs.service';
import { IUser } from 'app/core/user/user.model';
import { AccountService } from 'app/core/auth/account.service';
import { IOrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { OrdemDeFornecimentoDeleteDialogComponent } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento-delete-dialog.component';

@Component({
  selector: 'of-gerenciador-de-ofs',
  templateUrl: './gerenciador-de-ofs.component.html'
})
export class GerenciadorDeOfsComponent implements OnInit, OnDestroy {
  ordemDeFornecimentos?: IOrdemDeFornecimento[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  filtroPesquisa = { numeroOF: null, usuarioGestor: {} };
  usuariosGestor?: IUser[] | null;

  constructor(
    protected gerenciadorDeOfsService: GerenciadorDeOfsService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.gerenciadorDeOfsService.getUsuariosGestor().subscribe(response => {
      this.usuariosGestor = response.body;
      this.accountService.identity(false).subscribe(user => {
        this.filtroPesquisa.usuarioGestor = this.usuariosGestor!.filter(user1 => user1.login === user!.login)![0];
        this.activatedRoute.data.subscribe(data => {
          this.page = data.pagingParams.page;
          this.ascending = data.pagingParams.ascending;
          this.predicate = data.pagingParams.predicate;
          this.ngbPaginationPage = data.pagingParams.page;
          this.loadPage();
        });
      });
    });

    this.registerChangeInServicoOfs();
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page ? page : this.page;
    this.gerenciadorDeOfsService
      .queryByUser({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        numeroOF: this.filtroPesquisa.numeroOF,
        usuarioGestor: this.filtroPesquisa.usuarioGestor
      })
      .subscribe(
        (res: HttpResponse<IOrdemDeFornecimento[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  protected onSuccess(data: IOrdemDeFornecimento[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/gerenciador_de_ofs'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.ordemDeFornecimentos = data ? data : [];
  }

  registerChangeInServicoOfs(): void {
    this.eventSubscriber = this.eventManager.subscribe('servicoOfListModification', () => this.loadPage());
  }

  trackId(index: number, item: IOrdemDeFornecimento): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(ordemDeFornecimento: IOrdemDeFornecimento): void {
    const modalRef = this.modalService.open(OrdemDeFornecimentoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ordemDeFornecimento = ordemDeFornecimento;
    this.eventManager.subscribe('ordemDeFornecimentoListModification', () => this.loadPage());
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

  atualizaEstadoDaOf(ordemDeFornecimento: IOrdemDeFornecimento, estado: any): void {
    const estadoAnterior = ordemDeFornecimento.estado;
    ordemDeFornecimento.estado = estado;
    this.gerenciadorDeOfsService.updateEstadoDaOf(ordemDeFornecimento).subscribe(
      response => {
        ordemDeFornecimento.lastModifiedDate = response.body!.lastModifiedDate;
        ordemDeFornecimento.lastModifiedBy = response.body!.lastModifiedBy;
      },
      () => {
        ordemDeFornecimento.estado = estadoAnterior;
      }
    );
  }
}
