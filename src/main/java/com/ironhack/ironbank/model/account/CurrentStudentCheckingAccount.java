package com.ironhack.ironbank.model.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "current_student_checking_accounts")
public class CurrentStudentCheckingAccount extends CurrentAccount {

}
