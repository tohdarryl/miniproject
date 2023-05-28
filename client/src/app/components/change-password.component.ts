import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Account } from '../models/account';
import { AccountService } from '../services/account.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  form!:FormGroup
  constructor(private fb: FormBuilder, private router: Router, private accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService){}

  ngOnInit(): void {
    this.form = this.fb.group({
      oldpassword: this.fb.control<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
      newpassword: this.fb.control<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)])
    })
  }

  change(){
    this.alertService.info('Checking User Info');
    this.progressBar.startLoading();
    const oldPasswordFormVal = this.form?.value['oldpassword'];
    const newPasswordFormVal = this.form?.value['newpassword'];

    const a = {} as {
      email: String
      oldPassword: String;
      newPassword: String
    }
    a.email = this.accSvc.getSignedinUser();
    a.oldPassword = oldPasswordFormVal;
    a.newPassword = newPasswordFormVal;
   
  
    this.accSvc.changePassword(a)
    .then(n=>{
      this.progressBar.setSuccess();
      this.alertService.success('Password Changed');
      this.progressBar.completeLoading();
      // alert(`Login successful.`);
     
    })
    .catch(error => {
      this.progressBar.setError();
      this.alertService.danger('Invalid Old Password');
      this.progressBar.completeLoading();
      // alert(`Invalid email or/and password`);
      this.router.navigate(['/changepassword'])
    })
    this.router.navigate(['/changepassword'])
  }
}
