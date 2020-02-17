import {NgModule} from '@angular/core';
import {OfmanagerSharedLibsModule} from './shared-libs.module';
import {FindLanguageFromKeyPipe} from './language/find-language-from-key.pipe';
import {AlertComponent} from './alert/alert.component';
import {AlertErrorComponent} from './alert/alert-error.component';
import {LoginModalComponent} from './login/login.component';
import {HasAnyAuthorityDirective} from './auth/has-any-authority.directive';
import {ServicoOfDeleteDialogComponent} from 'app/entities/servico-of/servico-of-delete-dialog.component';
import {IConfig, NgxMaskModule} from 'ngx-mask';
import {ToastrModule} from 'ngx-toastr';
import {MaterialModule} from 'app/shared/material-module';

export const options: Partial<IConfig> | (() => Partial<IConfig>) = {};

@NgModule({
  imports: [OfmanagerSharedLibsModule, NgxMaskModule.forRoot(options), ToastrModule.forRoot()],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    ServicoOfDeleteDialogComponent
  ],
  entryComponents: [LoginModalComponent, ServicoOfDeleteDialogComponent],
  exports: [
    OfmanagerSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    MaterialModule
  ]
})
export class OfmanagerSharedModule {
}
