import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IArquivo, Arquivo } from 'app/shared/model/arquivo.model';
import { ArquivoService } from './arquivo.service';
import { ArquivoComponent } from './arquivo.component';
import { ArquivoDetailComponent } from './arquivo-detail.component';
import { ArquivoUpdateComponent } from './arquivo-update.component';

@Injectable({ providedIn: 'root' })
export class ArquivoResolve implements Resolve<IArquivo> {
  constructor(private service: ArquivoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArquivo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((arquivo: HttpResponse<Arquivo>) => {
          if (arquivo.body) {
            return of(arquivo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Arquivo());
  }
}

export const arquivoRoute: Routes = [
  {
    path: '',
    component: ArquivoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ofmanagerApp.arquivo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ArquivoDetailComponent,
    resolve: {
      arquivo: ArquivoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.arquivo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ArquivoUpdateComponent,
    resolve: {
      arquivo: ArquivoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.arquivo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ArquivoUpdateComponent,
    resolve: {
      arquivo: ArquivoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ofmanagerApp.arquivo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
