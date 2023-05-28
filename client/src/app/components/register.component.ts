import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Account } from '../models/account';

import { AccountService } from '../services/account.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form!: FormGroup

  constructor(private fb: FormBuilder, private router: Router, private accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService) { }

  // Build form object to link with html
  ngOnInit(): void {
    this.form = this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
      firstName: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),
      lastName: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),

    })
  }

  login() {
    console.log("Return to Login Page")
    this.router.navigate(['/login'])
  }

  back() {
    console.log("Return to View 0")
    this.router.navigate(['/'])
  }

  // create account in MySQL
  register() {
    this.alertService.info('Working on creating new account');
    this.progressBar.startLoading();
    const emailFormVal = this.form?.value['email'];
    const passwordFormVal = this.form?.value['password'];
    const fNameFormVal = this.form?.value['firstName'];
    const lNameFormVal = this.form?.value['lastName'];
    // instantiate a new Account interface
    const a = {} as Account;
    a.email = emailFormVal;
    a.password = passwordFormVal;
    a.firstName = fNameFormVal;
    a.lastName = lNameFormVal;

    console.log(">>> Account to save :", a)

    // this.accSvc.createAccount(a)

    this.accSvc.signup(a)
    .then(n=>{
      this.progressBar.setSuccess();
      this.alertService.success('Account Created. Account id is '+n.accountId);
      this.progressBar.completeLoading();
      // alert(`Your account has been created. Account id is ${n.accountId}`)
    })
    .catch((err: HttpErrorResponse) => {
      if(err.status==406){
        this.progressBar.setError();
        this.alertService.danger(a.email+' has been taken');
        this.progressBar.completeLoading();
        // alert(a.email+` has been taken. Please use another email.`);
        this.router.navigate(['/register']);

      } else if (err.status==500){
        this.progressBar.setError();
        this.alertService.danger('Server failed to save account');
        this.progressBar.completeLoading();
        alert(`Server failed to save account.`);
        this.router.navigate(['/register']);
      }
    
    });

    this.router.navigate(['/login']);
    
  }
}
