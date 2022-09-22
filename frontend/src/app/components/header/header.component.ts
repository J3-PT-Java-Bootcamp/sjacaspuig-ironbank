import { Component, OnDestroy, OnInit } from '@angular/core';
import { NbMenuService, NbSidebarService } from '@nebular/theme';
import { map } from 'rxjs/operators';

import { Subject, takeUntil } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  private destroy$: Subject<void> = new Subject<void>();
  userPictureOnly: boolean = false;
  user: any;
  userMenu = [
    // { title: 'Profile' }, // TODO: Add profile page
    { title: 'Log out' }
  ];

  constructor(
    private sidebarService: NbSidebarService,
    private menuService: NbMenuService,
    public authService: AuthService
  ) { }

  ngOnInit() {
    this.authService.user$.pipe(takeUntil(this.destroy$)).subscribe(user => this.user = user);

    this.menuService.onItemClick()
      .pipe(map(({ item: { title } }) => title))
      .subscribe(title => {
        if (title === 'Log out') {
          this.authService.logout();
        } else if (title === 'Profile') {
          // TODO: Navigate to profile page
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');

    return false;
  }

  navigateHome() {
    this.menuService.navigateHome();
    return false;
  }

}
