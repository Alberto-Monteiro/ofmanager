import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IServicoOf, ServicoOf } from 'app/shared/model/servico-of.model';
import { ServicoOfService } from './servico-of.service';
import { ServicoOfComponent } from './servico-of.component';
import { ServicoOfDetailComponent } from './servico-of-detail.component';
import { ServicoOfUpdateComponent } from './servico-of-update.component';

@Injectable({ providedIn: 'root' })
export class ServicoOfResolve implements Resolve<IServicoOf> {
  constructor(private service: ServicoOfService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServicoOf> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((servicoOf: HttpResponse<ServicoOf>) => {
          if (servicoOf.body) {
            return of(servicoOf.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServicoOf());
  }
}

export const servicoOfRoute: Routes = [
  {
    path: '',
    component: ServicoOfComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ServicoOfDetailComponent,
    resolve: {
      servicoOf: ServicoOfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ServicoOfUpdateComponent,
    resolve: {
      servicoOf: ServicoOfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ServicoOfUpdateComponent,
    resolve: {
      servicoOf: ServicoOfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.servicoOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
