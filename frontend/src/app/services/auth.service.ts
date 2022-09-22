import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthConfig, NullValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { ReplaySubject } from 'rxjs';
import { User } from '../models/user';
import { Role } from '../types/role';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public user$: ReplaySubject<User> = new ReplaySubject<User>(1);
  public role$: ReplaySubject<Role> = new ReplaySubject<Role>(1);

  constructor(
    private oauthService: OAuthService,
    private router: Router
  ) { }

  private authConfig: AuthConfig = {
    issuer: 'http://localhost:8181/realms/tutorial',
    redirectUri: window.location.origin,
    clientId: 'tutorial-frontend',
    responseType: 'code',
    scope: 'openid profile email offline_access',
    showDebugInformation: true,
  };

  public configure(): void {
    this.role$.next(this.getRole());
    this.oauthService.configure(this.authConfig);
    this.oauthService.tokenValidationHandler = new NullValidationHandler();
    this.oauthService.setupAutomaticSilentRefresh();
    this.oauthService.loadDiscoveryDocument().then(() => this.oauthService.tryLogin())
      .then(() => {
        if (this.oauthService.getIdentityClaims()) {
          this.user$.next(new User(this.getUsername(), this.getRole(), this.getUserId()));
          this.role$.next(this.getRole());
        }
      });
  }

  public login(): void {
    this.oauthService.initImplicitFlowInternal();
  }

  public logout(): void {
    this.oauthService.logOut();
    this.user$.next(new User('', 'public', ''));
  }

  public getIsLogged(): boolean {
    return (this.oauthService.hasValidIdToken() && this.oauthService.hasValidAccessToken());
  }

  public getUsername(): string {
    return (this.oauthService.getIdentityClaims() as any)['preferred_username'];
  }

  public getUserId(): string {
    return (this.oauthService.getIdentityClaims() as any)['sub'];
  }

  public getRole(): Role {
    const token = this.oauthService.getAccessToken();
    let roles: string[] = [];

    if(token) {
      const payload = token?.split('.')[1];
      const payloadDecodedJson = atob(payload);
      const payloadDecoded = JSON.parse(payloadDecodedJson);
      roles = payloadDecoded.realm_access.roles;
    }

    if (roles.includes('realm-super-admin')) {
      return 'super-admin';
    } else if (roles.includes('realm-admin')) {
      return 'admin';
    } else if (roles.includes('realm-user')) {
      return 'user';
    } else {
      return 'public';
    }
  }
}
