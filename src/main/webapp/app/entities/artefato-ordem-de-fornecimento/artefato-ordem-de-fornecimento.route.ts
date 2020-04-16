import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IArtefatoOrdemDeFornecimento, ArtefatoOrdemDeFornecimento } from 'app/shared/model/artefato-ordem-de-fornecimento.model';
import { ArtefatoOrdemDeFornecimentoService } from './artefato-ordem-de-fornecimento.service';
import { ArtefatoOrdemDeFornecimentoComponent } from './artefato-ordem-de-fornecimento.component';
import { ArtefatoOrdemDeFornecimentoDetailComponent } from './artefato-ordem-de-fornecimento-detail.component';
import { ArtefatoOrdemDeFornecimentoUpdateComponent } from './artefato-ordem-de-fornecimento-update.component';

@Injectable({ providedIn: 'root' })
export class ArtefatoOrdemDeFornecimentoResolve implements Resolve<IArtefatoOrdemDeFornecimento> {
  constructor(private service: ArtefatoOrdemDeFornecimentoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArtefatoOrdemDeFornecimento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((artefatoOrdemDeFornecimento: HttpResponse<ArtefatoOrdemDeFornecimento>) => {
          if (artefatoOrdemDeFornecimento.body) {
            return of(artefatoOrdemDeFornecimento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ArtefatoOrdemDeFornecimento());
  }
}

export const artefatoOrdemDeFornecimentoRoute: Routes = [
  {
    path: '',
    component: ArtefatoOrdemDeFornecimentoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'ofmanagerApp.artefatoOrdemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ArtefatoOrdemDeFornecimentoDetailComponent,
    resolve: {
      artefatoOrdemDeFornecimento: ArtefatoOrdemDeFornecimentoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.artefatoOrdemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ArtefatoOrdemDeFornecimentoUpdateComponent,
    resolve: {
      artefatoOrdemDeFornecimento: ArtefatoOrdemDeFornecimentoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.artefatoOrdemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ArtefatoOrdemDeFornecimentoUpdateComponent,
    resolve: {
      artefatoOrdemDeFornecimento: ArtefatoOrdemDeFornecimentoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.artefatoOrdemDeFornecimento.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
