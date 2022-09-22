import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Admin } from 'src/app/models/admin';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-create',
  templateUrl: './admin-create.component.html',
  styleUrls: ['./admin-create.component.scss']
})
export class AdminCreateComponent implements OnInit {

  @Output() refresh: EventEmitter<void> = new EventEmitter<void>();

  firstForm!: FormGroup;
  hiddenCompleted: boolean = true;
  showError: boolean = false;
  adminCreated: Admin | null = null;
  messageError: string = "An error has occurred, please try again later.";

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService
  ) { }

  ngOnInit(): void {

    this.firstForm = this.formBuilder.group({
      username: [null, [Validators.required]],
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      passwordConfirmation: [null, [Validators.required]],
    });
  }

  onFirstSubmit() {
    this.firstForm.markAsDirty();
  }

  createAdmin(): void {
    const admin: Admin = new Admin(
      undefined,
      this.firstForm.value.username,
      this.firstForm.value.email,
      this.firstForm.value.firstName,
      this.firstForm.value.lastName,
      this.firstForm.value.password
    );

    this.adminService.create(admin).subscribe(
      (admin: Admin) => {
        this.adminCreated = admin;
        this.hiddenCompleted = false;
        this.refresh.emit();
      },
      (error: HttpErrorResponse) => {
        this.showError = true;

        if (error.status === 409) {
          this.messageError = "The username or email is already in use.";
        } else {
          this.messageError = "Please try again later.";
        }
      }
    );
  }

  tryAgain(): void {
    this.firstForm.reset();
    this.hiddenCompleted = true;
    this.showError = false;
  }
}
