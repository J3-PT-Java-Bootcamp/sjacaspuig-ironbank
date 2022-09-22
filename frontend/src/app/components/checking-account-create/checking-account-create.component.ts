import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CheckingAccount } from 'src/app/models/checking-account';
import { Money } from 'src/app/models/money';
import { ThirdParty } from 'src/app/models/third-party';
import { CheckingAccountService } from 'src/app/services/checking-account.service';

@Component({
  selector: 'app-checking-account-create',
  templateUrl: './checking-account-create.component.html',
  styleUrls: ['./checking-account-create.component.scss']
})
export class CheckingAccountCreateComponent implements OnInit {

  firstForm!: FormGroup;
  hiddenCompleted: boolean = true;
  showError: boolean = false;
  accountCreated: CheckingAccount | null = null;
  messageError: string = "Please try again later.";
  messageStudentAccount: string = "The system created a student account because the primary owner is younger than 24 years old.";

  constructor(
    private formBuilder: FormBuilder,
    private checkingAccountService: CheckingAccountService
  ) { }

  ngOnInit(): void {

    this.firstForm = this.formBuilder.group({
      primaryOwner: [null, [Validators.required]],
      secondaryOwner: [null],
      balance: [null, [Validators.required]],
      secretKey: [null, [Validators.required]],
      secretKeyConfirmation: [null, [Validators.required]]
    });
  }

  onFirstSubmit() {
    this.firstForm.markAsDirty();
  }

  createThirdParty(): void {
    const money: Money = new Money(
      this.firstForm.get('balance')?.value
    );

    const account: CheckingAccount = new CheckingAccount(
      money,
      this.firstForm.get('secretKey')?.value,
      this.firstForm.get('primaryOwner')?.value,
      this.firstForm.get('secondaryOwner')?.value
    );

    this.checkingAccountService.create(account).subscribe(
      (account: CheckingAccount) => {
        this.accountCreated = account;
        this.hiddenCompleted = false;
      },
      (error: HttpErrorResponse) => {
        this.showError = true;
        this.messageError = error.error.message;
      }
    );
  }

  tryAgain(): void {
    this.firstForm.reset();
    this.hiddenCompleted = true;
    this.showError = false;
  }
}
