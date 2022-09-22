import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PagesRoutingModule } from './pages-routing.module';
import { PagesComponent } from './pages.component';
import { LayoutModule } from '../components/layout/layout.module';
import { NbMenuModule, NbSidebarModule, NbThemeModule } from '@nebular/theme';


@NgModule({
  declarations: [
    PagesComponent
  ],
  imports: [
    CommonModule,
    PagesRoutingModule,
    LayoutModule,
    NbMenuModule.forRoot(),
    NbSidebarModule.forRoot(),
    NbThemeModule.forRoot(),
  ]
})
export class PagesModule { }
