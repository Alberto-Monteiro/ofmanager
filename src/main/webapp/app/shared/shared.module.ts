import { NgModule } from '@angular/core';
import { OfmanagerSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { OrdemDeFornecimentoDeleteDialogComponent } from 'app/entities/ordem-de-fornecimento/ordem-de-fornecimento-delete-dialog.component';
import { IConfig, NgxMaskModule } from 'ngx-mask';
import { ToastrModule } from 'ngx-toastr';
import { MaterialModule } from 'app/shared/material-module';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ClipboardModule } from '@angular/cdk/clipboard';

export const options: Partial<IConfig> | (() => Partial<IConfig>) = {};

@NgModule({
  imports: [OfmanagerSharedLibsModule, NgxMaskModule.forRoot(options), ToastrModule.forRoot()],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    OrdemDeFornecimentoDeleteDialogComponent
  ],
  entryComponents: [LoginModalComponent, OrdemDeFornecimentoDeleteDialogComponent],
  exports: [
    OfmanagerSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    MaterialModule,
    LoadingBarHttpClientModule,
    LoadingBarModule,
    FlexLayoutModule,
    NgxMaskModule,
    ClipboardModule
  ]
})
export class OfmanagerSharedModule {}
