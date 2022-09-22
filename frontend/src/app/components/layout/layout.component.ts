import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { Role } from 'src/app/types/role';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

  constructor(
    public authService: AuthService
  ) { }

  ngOnInit(): void {
  }

}
