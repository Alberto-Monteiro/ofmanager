import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IArquivoDaOf, ArquivoDaOf } from 'app/shared/model/arquivo-da-of.model';
import { ArquivoDaOfService } from './arquivo-da-of.service';
import { ArquivoDaOfComponent } from './arquivo-da-of.component';
import { ArquivoDaOfDetailComponent } from './arquivo-da-of-detail.component';
import { ArquivoDaOfUpdateComponent } from './arquivo-da-of-update.component';

@Injectable({ providedIn: 'root' })
export class ArquivoDaOfResolve implements Resolve<IArquivoDaOf> {
  constructor(private service: ArquivoDaOfService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArquivoDaOf> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((arquivoDaOf: HttpResponse<ArquivoDaOf>) => {
          if (arquivoDaOf.body) {
            return of(arquivoDaOf.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ArquivoDaOf());
  }
}

export const arquivoDaOfRoute: Routes = [
  {
    path: '',
    component: ArquivoDaOfComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ofmanagerApp.arquivoDaOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ArquivoDaOfDetailComponent,
    resolve: {
      arquivoDaOf: ArquivoDaOfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.arquivoDaOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ArquivoDaOfUpdateComponent,
    resolve: {
      arquivoDaOf: ArquivoDaOfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.arquivoDaOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ArquivoDaOfUpdateComponent,
    resolve: {
      arquivoDaOf: ArquivoDaOfResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.arquivoDaOf.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
