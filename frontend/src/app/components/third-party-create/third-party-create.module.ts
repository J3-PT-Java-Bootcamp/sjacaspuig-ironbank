import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThirdPartyCreateComponent } from './third-party-create.component';
import { NbButtonModule, NbCardModule, NbInputModule } from '@nebular/theme';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    ThirdPartyCreateComponent
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
    ThirdPartyCreateComponent
  ]
})
export class ThirdPartyCreateModule { }
