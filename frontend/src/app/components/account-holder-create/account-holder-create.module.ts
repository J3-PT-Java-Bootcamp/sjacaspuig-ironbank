import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountHolderCreateComponent } from './account-holder-create.component';
import { NbButtonModule, NbCardModule, NbDatepickerModule, NbInputModule, NbStepperModule } from '@nebular/theme';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AccountHolderCreateComponent
  ],
  imports: [
    CommonModule,
    NbStepperModule,
    NbCardModule,
    FormsModule,
    ReactiveFormsModule,
    NbButtonModule,
    NbInputModule,
    NbDatepickerModule
  ],
  exports: [
    AccountHolderCreateComponent
  ]
})
export class AccountHolderCreateModule { }
