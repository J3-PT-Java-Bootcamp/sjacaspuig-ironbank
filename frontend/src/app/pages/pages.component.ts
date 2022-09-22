import { Component, OnDestroy, OnInit } from '@angular/core';
import { NbMenuItem } from '@nebular/theme';
import { Subject, takeUntil, tap } from 'rxjs';
import { ADMIN_MENU } from '../constants/admin-menu';
import { SUPER_ADMIN_MENU } from '../constants/super-admin-menu';
import { USER_MENU } from '../constants/user-menu';
import { AuthService } from '../services/auth.service';
import { Role } from '../types/role';

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.scss']
})
export class PagesComponent implements OnInit, OnDestroy {

  public menu: NbMenuItem[] = [];
  private destroy$: Subject<void> = new Subject<void>();

  constructor(
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.authService.role$.pipe(takeUntil(this.destroy$))
      .subscribe(role => this.pickupMenu(role));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  pickupMenu(role: Role) {
    switch (role) {
      case 'super-admin':
        this.menu = SUPER_ADMIN_MENU;
        break;
      case 'admin':
        this.menu = ADMIN_MENU;
        break;
      case 'user':
        this.menu = USER_MENU;
        break;
      default:
        this.menu = [];
    }
  }

}
