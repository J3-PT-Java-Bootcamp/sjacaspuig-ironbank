import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThirdParty } from 'src/app/models/third-party';
import { ThirdPartyService } from 'src/app/services/third-party.service';

@Component({
  selector: 'app-third-party-create',
  templateUrl: './third-party-create.component.html',
  styleUrls: ['./third-party-create.component.scss']
})
export class ThirdPartyCreateComponent implements OnInit {

  @Output() refresh: EventEmitter<void> = new EventEmitter<void>();

  firstForm!: FormGroup;
  hiddenCompleted: boolean = true;
  showError: boolean = false;
  thirdPartyCreated: ThirdParty | null = null;
  messageError: string = "An error has occurred, please try again later.";

  constructor(
    private formBuilder: FormBuilder,
    private thirdPartyService: ThirdPartyService
  ) { }

  ngOnInit(): void {

    this.firstForm = this.formBuilder.group({
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      hashedKey: [null, [Validators.required]]
    });
  }

  onFirstSubmit() {
    this.firstForm.markAsDirty();
  }

  createThirdParty(): void {
    const thirdParty: ThirdParty = new ThirdParty(
      undefined,
      this.firstForm.value.firstName,
      this.firstForm.value.lastName,
      this.firstForm.value.hashedKey
    );

    this.thirdPartyService.create(thirdParty).subscribe(
      (thirdParty: ThirdParty) => {
        this.thirdPartyCreated = thirdParty;
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
