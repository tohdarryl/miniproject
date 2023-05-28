import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { AccountService } from '../services/account.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit{

  form!:FormGroup
  
  constructor(private fb: FormBuilder, private router: Router, private accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService){}

  ngOnInit(): void {
    this.form = this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email])
      
    })
  }

  reset(){
    this.alertService.info('Checking User Info');
    this.progressBar.startLoading();
    const emailFormVal = this.form?.value['email'];
    const a = {} as {
      email: String
    }
    a.email = emailFormVal;
   
  
    this.accSvc.forgotPassword(a)
    .then(n=>{
      this.progressBar.setSuccess();
      this.alertService.success('Check your email for Credentials');
      this.progressBar.completeLoading();
      // alert(`Login successful.`);
     
    })
    .catch(error => {
      this.progressBar.setError();
      this.alertService.danger('Invalid Email');
      this.progressBar.completeLoading();
      // alert(`Invalid email or/and password`);
      this.router.navigate(['/forgotpassword'])
    })

  }
}
