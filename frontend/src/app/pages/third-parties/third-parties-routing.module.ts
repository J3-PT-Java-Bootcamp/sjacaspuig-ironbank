import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ThirdPartiesComponent } from './third-parties.component';

const routes: Routes = [
  {
    path: '',
    component: ThirdPartiesComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ThirdPartiesRoutingModule { }
