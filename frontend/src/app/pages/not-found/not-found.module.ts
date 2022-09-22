import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotFoundComponent } from './not-found.component';
import { NbButtonModule, NbCardModule } from '@nebular/theme';
import { NotFoundRoutingModule } from './not-found-routing.module';



@NgModule({
  declarations: [
    NotFoundComponent
  ],
  imports: [
    CommonModule,
    NbCardModule,
    NbButtonModule,
    NotFoundRoutingModule
  ]
})
export class NotFoundModule { }
