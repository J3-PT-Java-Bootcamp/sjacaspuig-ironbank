import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout.component';
import { NbLayoutModule, NbSidebarModule } from '@nebular/theme';
import { FooterModule } from '../footer/footer.module';
import { HeaderModule } from '../header/header.module';



@NgModule({
  declarations: [
    LayoutComponent
  ],
  imports: [
    CommonModule,
    NbLayoutModule,
    NbSidebarModule,
    FooterModule,
    HeaderModule
  ],
  exports: [
    LayoutComponent
  ]
})
export class LayoutModule { }
