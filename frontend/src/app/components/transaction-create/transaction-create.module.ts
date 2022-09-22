import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionCreateComponent } from './transaction-create.component';
import { NbButtonModule, NbCardModule, NbCheckboxModule, NbInputModule } from '@nebular/theme';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    TransactionCreateComponent
  ],
  imports: [
    CommonModule,
    NbCardModule,
    FormsModule,
    ReactiveFormsModule,
    NbButtonModule,
    NbInputModule,
    NbCheckboxModule
  ],
  exports: [
    TransactionCreateComponent
  ]
})
export class TransactionCreateModule { }
