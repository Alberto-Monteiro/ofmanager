import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { OfmanagerSharedModule } from 'app/shared/shared.module';
import { OfmanagerCoreModule } from 'app/core/core.module';
import { OfmanagerAppRoutingModule } from './app-routing.module';
import { OfmanagerHomeModule } from './home/home.module';
import { OfmanagerEntityModule } from './entities/entity.module';
import { OfmanagerGerenciadorDeOfsModule } from 'app/gerenciadordeofs/gerenciador-de-ofs.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    OfmanagerSharedModule,
    OfmanagerCoreModule,
    OfmanagerHomeModule,
    OfmanagerGerenciadorDeOfsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    OfmanagerEntityModule,
    OfmanagerAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class OfmanagerAppModule {}
