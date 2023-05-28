import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { AccountService } from '../services/account.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';




@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{
  form!: FormGroup
  myImage: String = "assets/images/FoodCollage.jpg";


  constructor(private fb: FormBuilder, private router: Router, private accSvc: AccountService,
              private dialog: MatDialog,
              public progressBar: ProgressBarService, private alertService: AlertService){}
  

  // Build form object to link with html
  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
   
  } 
    
  
  
  search(){
    // to take value from formControlName="name"

    const n = this.form?.value['name']
    console.log(">>> name of place: ", n)
    // same as in app-routing.module.ts to route to list component
    if(this.accSvc.isTokenExpired()){
      this.alertService.danger('Login required');
      this.router.navigate(['/login'])
    } else {
      this.router.navigate(['/list', n])
      
    }
  }
  
  clear(){
    this.form?.reset();
  }

  login(){
    console.log("Go to login view")
    this.router.navigate(['/login'])
  }

  logout(){
    this.accSvc.signout()
  }
}
