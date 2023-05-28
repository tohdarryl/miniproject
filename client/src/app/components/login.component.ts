import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Account } from '../models/account';
import { AccountService } from '../services/account.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  
  account!: Account
  form!:FormGroup
  hide = true;

  constructor(private fb: FormBuilder, private router: Router, private accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService){}


  // Build form object to link with html
  ngOnInit(): void {
    this.form = this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
      
    })
  }

  passwordInput() { 
    return this.form.get('password'); 
  }  

  login(){
    this.alertService.info('Checking User Info');
    this.progressBar.startLoading();
    const emailFormVal = this.form?.value['email'];
    const passwordFormVal = this.form?.value['password'];

    const a = {} as Account
    a.email = emailFormVal;
    a.password = passwordFormVal;
   
  
    this.accSvc.signin(a)
    .then(n=>{
      this.progressBar.setSuccess();
      this.alertService.success('Logged In');
      this.progressBar.completeLoading(); 
      // alert(`Login successful.`);
     
    })
    .catch(error => {
      this.progressBar.setError();
      this.alertService.danger('Invalid email or password');
      this.progressBar.completeLoading();
      // alert(`Invalid email or/and password`);
      this.router.navigate(['/login'])
    })

    // this.accSvc.retrieveAccount(emailFormVal, passwordFormVal)
    // .then( (result) => { 
    //   this.account = result.details
    //   //shows pop-up of accountId from Spring-boot on localhost:4200, if successful
    //   alert(`Login successful. Account id is ${this.account.accountId}`)
    // })
    // .catch(error => {
    //   alert(`Invalid email or/and password`)
    //   // converts error into JSON String
    //   // alert(`ERROR! ${JSON.stringify(error)}`)
    //   this.router.navigate(['/login'])
    // })
    
    
    // this.router.navigate(['/list', n])
    this.router.navigate(['/'])
  }

  register(){
    this.router.navigate(['/register'])
  }

  back(){
    console.log("Return to View 0")
    this.router.navigate(['/'])
  }

  forgotPassword(){
    this.router.navigate(['/forgotpassword'])
  }

  
}
