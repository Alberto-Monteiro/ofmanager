import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOrdemDeFornecimento, OrdemDeFornecimento } from 'app/shared/model/ordem-de-fornecimento.model';
import { OrdemDeFornecimentoService } from './ordem-de-fornecimento.service';
import { OrdemDeFornecimentoComponent } from './ordem-de-fornecimento.component';
import { OrdemDeFornecimentoDetailComponent } from './ordem-de-fornecimento-detail.component';
import { OrdemDeFornecimentoUpdateComponent } from './ordem-de-fornecimento-update.component';

@Injectable({ providedIn: 'root' })
export class OrdemDeFornecimentoResolve implements Resolve<IOrdemDeFornecimento> {
  constructor(private service: OrdemDeFornecimentoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdemDeFornecimento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ordemDeFornecimento: HttpResponse<OrdemDeFornecimento>) => {
          if (ordemDeFornecimento.body) {
            return of(ordemDeFornecimento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrdemDeFornecimento());
  }
}

export const ordemDeFornecimentoRoute: Routes = [
  {
    path: '',
    component: OrdemDeFornecimentoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'ofmanagerApp.ordemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: OrdemDeFornecimentoDetailComponent,
    resolve: {
      ordemDeFornecimento: OrdemDeFornecimentoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.ordemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: OrdemDeFornecimentoUpdateComponent,
    resolve: {
      ordemDeFornecimento: OrdemDeFornecimentoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.ordemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OrdemDeFornecimentoUpdateComponent,
    resolve: {
      ordemDeFornecimento: OrdemDeFornecimentoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.ordemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
