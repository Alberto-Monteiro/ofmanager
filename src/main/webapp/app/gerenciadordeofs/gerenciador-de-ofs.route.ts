import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { GerenciadorDeOfsService } from './gerenciador-de-ofs.service';
import { GerenciadorDeOfsComponent } from './gerenciador-de-ofs.component';
import { GerenciadorDeOfsUpdateComponent } from 'app/gerenciadordeofs/gerenciador-de-ofs-update.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOrdemFornecimento, OrdemFornecimento } from 'app/shared/model/ordem-fornecimento.model';
import { ServicoOf } from 'app/shared/model/servico-of.model';

@Injectable({ providedIn: 'root' })
export class GerenciadorDeOfsResolve implements Resolve<IOrdemFornecimento> {
  constructor(private gerenciadorDeOfsService: GerenciadorDeOfsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdemFornecimento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.gerenciadorDeOfsService.find(id).pipe(
        flatMap((ordemFornecimento: HttpResponse<OrdemFornecimento>) => {
          if (ordemFornecimento.body) {
            return of(ordemFornecimento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrdemFornecimento('', new ServicoOf()));
  }
}

export const gerenciadorDeOfsRoute: Routes = [
  {
    path: 'gerenciador_de_ofs',
    component: GerenciadorDeOfsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_GESTOR_OF'],
      defaultSort: 'lastModifiedDate,desc',
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'gerenciador_de_ofs/new',
    component: GerenciadorDeOfsUpdateComponent,
    resolve: {
      ordemFornecimento: GerenciadorDeOfsResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_GESTOR_OF'],
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'gerenciador_de_ofs/:id/edit',
    component: GerenciadorDeOfsUpdateComponent,
    resolve: {
      ordemFornecimento: GerenciadorDeOfsResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_GESTOR_OF'],
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
