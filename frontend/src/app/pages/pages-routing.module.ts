import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../guards/auth.guard';
import { HomeGuard } from '../guards/home.guard';
import { PagesComponent } from './pages.component';

const routes: Routes = [
  {
    path: '',
    component: PagesComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('./home/home.module').then(m => m.HomeModule),
        canActivate: [HomeGuard]
      },
      { 
        path: 'dashboard-user',
        loadChildren: () => import('./account-holder-dashboard/dashboard-user.module').then(m => m.DashboardUserModule),
        canActivate: [AuthGuard],
        data: {requiredRoles: ['user']}
      },
      {
        path: 'transactions',
        loadChildren: () => import('./transactions/transactions.module').then(m => m.TransactionsModule),
        canActivate: [AuthGuard],
        data: {requiredRoles: ['admin']}
      },
      {
        path: 'users',
        loadChildren: () => import('./account-holders/users.module').then(m => m.UsersModule),
        canActivate: [AuthGuard],
        data: {requiredRoles: ['super-admin', 'admin']}
      },
      {
        path: 'admins',
        loadChildren: () => import('./admins/admins.module').then(m => m.AdminsModule),
        canActivate: [AuthGuard],
        data: {requiredRoles: ['super-admin']}
      },
      {
        path: 'accounts',
        loadChildren: () => import('./accounts/accounts.module').then(m => m.AccountsModule),
        canActivate: [AuthGuard],
        data: {requiredRoles: ['admin']}
      },
      {
        path: 'third-parties',
        loadChildren: () => import('./third-parties/third-parties.module').then(m => m.ThirdPartiesModule),
        canActivate: [AuthGuard],
        data: {requiredRoles: ['admin']}
      },
      { path: '**', loadChildren: () => import('./not-found/not-found.module').then(m => m.NotFoundModule) },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
