import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminCreateComponent } from './admin-create.component';
import { NbButtonModule, NbCardModule, NbInputModule } from '@nebular/theme';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AdminCreateComponent
  ],
  imports: [
    CommonModule,
    NbCardModule,
    FormsModule,
    ReactiveFormsModule,
    NbButtonModule,
    NbInputModule,
  ],
  exports: [
    AdminCreateComponent
  ]
})
export class AdminCreateModule { }
