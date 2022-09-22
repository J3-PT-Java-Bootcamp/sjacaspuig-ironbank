
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Role } from '../types/role';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    if (!this.authService.getIsLogged()) {
      this.router.navigate(['/']);
      this.authService.logout();
      return false;
    }
    
    const requiredRoles: Role = next.data['requiredRoles'];
    const realRol: Role = this.authService.getRole();

    if (requiredRoles.indexOf(realRol) === -1) {
      this.router.navigate(['/']);
      this.authService.logout();
      return false;
    }

    return true;
  }

}
