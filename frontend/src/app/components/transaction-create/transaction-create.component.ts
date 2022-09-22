import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Money } from 'src/app/models/money';
import { Transaction } from 'src/app/models/transaction';
import { AuthService } from 'src/app/services/auth.service';
import { TransactionService } from 'src/app/services/transaction.service';
import { TransactionType } from 'src/app/types/transaction-type';

@Component({
  selector: 'app-transaction-create',
  templateUrl: './transaction-create.component.html',
  styleUrls: ['./transaction-create.component.scss']
})
export class TransactionCreateComponent implements OnInit {

  @Output() refresh: EventEmitter<void> = new EventEmitter<void>();

  firstForm!: FormGroup;
  hiddenCompleted: boolean = true;
  showError: boolean = false;
  transactionCreated: Transaction | null = null;
  messageError: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private transactionService: TransactionService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {

    this.firstForm = this.formBuilder.group({
      name: [null, [Validators.required]],
      amount: [null, [Validators.required]],
      type: [false],
      sourceAccount: [null],
      targetAccount: [null],
      fee: [null],
      secretKey: [null],
      hashedKey: [null],
      concept: [null]
    });
  }

  onFirstSubmit() {
    this.firstForm.markAsDirty();
  }

  createThirdParty(): void {

    const amount: Money = new Money(
      this.firstForm.get('amount')?.value
    );

    const fee: Money | undefined = this.firstForm.get('fee')?.value ? new Money(this.firstForm.get('fee')?.value) : undefined;

    const type: TransactionType = this.firstForm.get('type')?.value ? 'ADMIN_MOVEMENT' : 'TRANSFER';

    const thirdParty: Transaction = new Transaction(
      this.firstForm.get('name')?.value,
      amount,
      type,
      this.authService.getUserId(),
      this.firstForm.get('sourceAccount')?.value,
      this.firstForm.get('targetAccount')?.value,
      fee,
      this.firstForm.get('secretKey')?.value,
      this.firstForm.get('hashedKey')?.value,
      this.firstForm.get('concept')?.value
    );

    this.transactionService.create(thirdParty).subscribe(
      (transaction: Transaction) => {
        
        if (transaction.status === 'COMPLETED') {
          this.transactionCreated = transaction;
          this.hiddenCompleted = false;
        } else {
          this.messageError = transaction.failureReason ? transaction.failureReason : '';
          this.showError = true;
        }

        this.refresh.emit();

      },
      (error: HttpErrorResponse) => {
        const transaction: Transaction = error.error;

        this.messageError = transaction.failureReason ? transaction.failureReason : '';
        this.showError = true;
      }
    );
  }

  tryAgain(): void {
    this.firstForm.reset();
    this.hiddenCompleted = true;
    this.showError = false;
  }
}
