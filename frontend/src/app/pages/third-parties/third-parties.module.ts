import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ThirdPartiesRoutingModule } from './third-parties-routing.module';
import { ThirdPartiesComponent } from './third-parties.component';
import { ThirdPartyCreateModule } from 'src/app/components/third-party-create/third-party-create.module';
import { LayoutTwoColumnsModule } from 'src/app/components/layout-two-columns/layout-two-columns.module';
import { NbButtonModule, NbCardModule, NbIconModule, NbListModule, NbUserModule } from '@nebular/theme';


@NgModule({
  declarations: [
    ThirdPartiesComponent
  ],
  imports: [
    CommonModule,
    ThirdPartiesRoutingModule,
    ThirdPartyCreateModule,
    LayoutTwoColumnsModule,
    NbButtonModule,
    NbListModule,
    NbCardModule,
    NbIconModule,
    NbUserModule
  ]
})
export class ThirdPartiesModule { }
