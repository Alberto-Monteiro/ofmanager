import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IArtefato, Artefato } from 'app/shared/model/artefato.model';
import { ArtefatoService } from './artefato.service';
import { ArtefatoComponent } from './artefato.component';
import { ArtefatoDetailComponent } from './artefato-detail.component';
import { ArtefatoUpdateComponent } from './artefato-update.component';

@Injectable({ providedIn: 'root' })
export class ArtefatoResolve implements Resolve<IArtefato> {
  constructor(private service: ArtefatoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArtefato> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((artefato: HttpResponse<Artefato>) => {
          if (artefato.body) {
            return of(artefato.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Artefato());
  }
}

export const artefatoRoute: Routes = [
  {
    path: '',
    component: ArtefatoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'ofmanagerApp.artefato.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ArtefatoDetailComponent,
    resolve: {
      artefato: ArtefatoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.artefato.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ArtefatoUpdateComponent,
    resolve: {
      artefato: ArtefatoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.artefato.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ArtefatoUpdateComponent,
    resolve: {
      artefato: ArtefatoResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ofmanagerApp.artefato.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
