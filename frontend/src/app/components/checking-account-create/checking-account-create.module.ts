import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckingAccountCreateComponent } from './checking-account-create.component';
import { NbButtonModule, NbCardModule, NbInputModule } from '@nebular/theme';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    CheckingAccountCreateComponent
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
    CheckingAccountCreateComponent
  ]
})
export class CheckingAccountCreateModule { }
