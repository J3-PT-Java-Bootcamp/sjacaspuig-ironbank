
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Role } from '../types/role';

@Injectable({
  providedIn: 'root'
})
export class HomeGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    const isLogged: boolean = this.authService.getIsLogged();
    console.log('isLogged: ', isLogged);

    if (isLogged) {
      const realRol: Role = this.authService.getRole();
      
      if (realRol === 'user') {
        this.router.navigate(['/dashboard-user']);
      } else if (realRol === 'admin') {
        this.router.navigate(['/users']);
      } else if (realRol === 'super-admin') {
        this.router.navigate(['/admins']);
      } else {
        this.authService.logout();
        this.router.navigate(['/']);
      }

      return false;
    }

    this.authService.logout();
    return true;
  }

}
