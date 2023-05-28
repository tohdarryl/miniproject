import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { NgProgress } from 'ngx-progressbar';
import { AccountService } from 'src/app/services/account.service';
import { ProgressBarService } from '../../services/progress-bar.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit{

  form!: FormGroup
  accountId = ''


  constructor(private router: Router, public accSvc: AccountService,
    private dialog: MatDialog, private progress: NgProgress, public progressBar: ProgressBarService,
    private fb: FormBuilder, private alertService: AlertService){}
    
  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    }),
    this.progressBar.progressRef = this.progress.ref('progressBar');
  }

  register(){
    this.router.navigate(['/register'])
  }
  login(){
    console.log("Go to login view")
    this.router.navigate(['/login'])
  }

  logout(){
    this.accSvc.signout()
  }

  back(){
    console.log("Return to View 0")
    this.router.navigate(['/'])
  }

  changePassword(){
    this.router.navigate(['/changepassword'])
  }

  getCollection(){
    this.accountId = this.accSvc.decodedToken.accountId
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['/collection',this.accountId])

    });
    
  }

  getUsers(){
    if(this.accSvc.isTokenExpired()){
      this.form.reset()
      this.alertService.danger('Login required');
      this.router.navigate(['/login'])
    } else {
    const n = this.form?.value['name']
    console.log("input >>> ", n)
    this.router.navigate(['/users', n])
    this.form.reset()
    }
  }

}
