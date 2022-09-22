import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountHolder } from 'src/app/models/account-holder';
import { Address } from 'src/app/models/address';
import { AccountHolderService } from 'src/app/services/account-holder.service';

@Component({
  selector: 'app-account-holder-create',
  templateUrl: './account-holder-create.component.html',
  styleUrls: ['./account-holder-create.component.scss']
})
export class AccountHolderCreateComponent implements OnInit {

  @Output() refresh: EventEmitter<void> = new EventEmitter<void>();

  firstForm!: FormGroup;
  secondForm!: FormGroup;
  thirdForm!: FormGroup;
  fourthForm!: FormGroup;
  hiddenCompleted: boolean = true;
  hiddenError: boolean = true;
  userCreated: AccountHolder | null = null;
  messageError: string = "An error has occurred, please try again later.";

  constructor(
    private formBuilder: FormBuilder,
    private accountHolderService: AccountHolderService
  ) { }

  ngOnInit(): void {

    this.firstForm = this.formBuilder.group({
      username: [null, [Validators.required]],
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      birthDate: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]]
    });

    this.secondForm = this.formBuilder.group({
      street: [null, [Validators.required]],
      number: [null, [Validators.required]],
      extraInformation: [null],
      postalCode: [null, [Validators.required]],
      city: [null, [Validators.required]],
      country: [null, [Validators.required]],
      province: [null],
    });

    this.thirdForm = this.formBuilder.group({
      street: [null],
      number: [null],
      extraInformation: [null],
      postalCode: [null],
      city: [null],
      country: [null],
      province: [null],
    });

    this.fourthForm = this.formBuilder.group({
      password: [null, [Validators.required]],
      passwordConfirmation: [null, [Validators.required]],
    });
  }

  onFirstSubmit() {
    this.firstForm.markAsDirty();
  }

  onSecondSubmit() {
    this.secondForm.markAsDirty();
  }

  onThirdSubmit() {
    this.thirdForm.markAsDirty();
  }

  onFourthSubmit() {
    this.fourthForm.markAsDirty();
  }

  createUser(): void {
    const primaryAddress = new Address(
      this.secondForm.get('street')?.value,
      this.secondForm.get('number')?.value,
      this.secondForm.get('extraInformation')?.value,
      this.secondForm.get('postalCode')?.value,
      this.secondForm.get('city')?.value,
      this.secondForm.get('country')?.value,
      this.secondForm.get('province')?.value
    );

    let secondaryAddress: Address | null = null;
    if (
      this.thirdForm.get('street')?.value != null &&
      this.thirdForm.get('number')?.value != null &&
      this.thirdForm.get('postalCode')?.value != null &&
      this.thirdForm.get('city')?.value != null &&
      this.thirdForm.get('country')?.value != null
    ) {
      secondaryAddress = new Address(
        this.thirdForm.get('street')?.value,
        this.thirdForm.get('number')?.value,
        this.thirdForm.get('extraInformation')?.value,
        this.thirdForm.get('postalCode')?.value,
        this.thirdForm.get('city')?.value,
        this.thirdForm.get('country')?.value,
        this.thirdForm.get('province')?.value
      );
    }

    const accountHolder = new AccountHolder(
      undefined,
      this.firstForm.get('username')?.value,
      this.firstForm.get('email')?.value,
      this.firstForm.get('firstName')?.value,
      this.firstForm.get('lastName')?.value,
      this.firstForm.get('birthDate')?.value,
      primaryAddress,
      secondaryAddress,
      this.fourthForm.get('password')?.value
    )

    this.accountHolderService.create(accountHolder).subscribe(
      (user: AccountHolder) => {
        this.hiddenCompleted = false;
        this.userCreated = user;
        this.refresh.emit();
      },
      (error: HttpErrorResponse) => {
        this.hiddenError = false;

        if (error.status === 409) {
          this.messageError = "The username or email is already in use.";
        } else {
          this.messageError = "Please try again later.";
        }
      }
    )
  }

  tryAgain(): void {
    this.firstForm.reset();
    this.hiddenCompleted = true;
    this.hiddenError = false;
  }

}
