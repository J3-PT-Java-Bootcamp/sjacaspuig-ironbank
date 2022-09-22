import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header.component';
import { NbActionsModule, NbContextMenuModule, NbIconModule, NbSearchModule, NbSelectModule, NbUserModule } from '@nebular/theme';



@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    NbIconModule,
    NbSelectModule,
    NbActionsModule,
    NbUserModule,
    NbSearchModule,
    NbContextMenuModule,
    NbIconModule
  ],
  exports: [
    HeaderComponent
  ]
})
export class HeaderModule { }
