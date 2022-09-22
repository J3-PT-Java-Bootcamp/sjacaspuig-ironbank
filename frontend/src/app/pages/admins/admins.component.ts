import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { map, Observable } from 'rxjs';
import { Admin } from 'src/app/models/admin';

@Component({
  selector: 'app-admins',
  templateUrl: './admins.component.html',
  styleUrls: ['./admins.component.scss']
})
export class AdminsComponent implements OnInit {

  public admins$!: Observable<Admin[]>;

  constructor(
    private adminService: AdminService
  ) { }

  ngOnInit(): void {
    this.getAdmins();
  }

  deleteAdmin(id: string | undefined): void {
    if (confirm("Are you sure?") && id) {
      this.adminService.deleteAdmin(id).subscribe(() => this.getAdmins());
    }
  }

  getAdmins() {
    this.admins$ = this.adminService.getAll();
  }
}
