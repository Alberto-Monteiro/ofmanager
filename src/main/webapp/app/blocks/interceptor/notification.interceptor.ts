import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class NotificationInterceptor implements HttpInterceptor {
  constructor(private toastrService: ToastrService, private translateService: TranslateService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          let alert: string | null = null;
          let alertParams: string | null = null;

          event.headers.keys().forEach(entry => {
            if (entry.toLowerCase().endsWith('app-alert')) {
              alert = event.headers.get(entry);
            } else if (entry.toLowerCase().endsWith('app-params')) {
              alertParams = decodeURIComponent(event.headers.get(entry)!.replace(/\+/g, ' '));
            }
          });

          if (alert) {
            this.translateService.get(alert, { param: alertParams }).subscribe(value => {
              this.toastrService.toastrConfig.positionClass = 'toast-top-center';
              this.toastrService.toastrConfig.maxOpened = 1;
              this.toastrService.toastrConfig.autoDismiss = true;
              this.toastrService.success(value);
            });
          }
        }
      })
    );
  }
}
