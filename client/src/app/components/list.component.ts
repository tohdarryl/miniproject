import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatListOption, MatSelectionListChange } from '@angular/material/list';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Subscription } from 'rxjs';
import { Place } from '../models/place';
import { AccountService } from '../services/account.service';
import { PlaceService } from '../services/place.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  query = ""
  param$!: Subscription;
  places!: Place[]
  currentIndex: number = 0;
  selected!: any[];
  accountId!: String;



  constructor(private activatedRoute: ActivatedRoute, private placeSvc: PlaceService,
    private router: Router, private accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService) { }

  ngOnInit(): void {
    console.log("list component")
    // to get value from /list/<query> from search.component.ts
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        // app-routing.module.ts;  path: "list/:query", component: ListComponent
        this.query = params['query']
        console.log(">>> query: ", this.query)
        const l = await this.placeSvc.getPlaces(this.query)

        // console.log(l)

        // if (l == undefined || l.length == 0) {
        //   // route back to search.component if result is undefined or empty
        //   this.router.navigate(['/'])
        // } else {
          // set as result as Place[] if ok
          this.places = l
        // }
      }
    )

  }

  onGroupsChange(options: MatListOption[]) {
    // map these MatListOptions to their values
    console.log(options.map(o => o.value));
    this.selected = options.map(o => o.value)
  }

  save(){
  console.log("selected = ", this.selected)
  this.accountId = this.accSvc.decodedToken?.accountId
  console.log("accountId = "+ this.accountId)
  if(this.selected.length > 0){
    this.alertService.info('Saving...');
    this.progressBar.startLoading();
  this.placeSvc.savePlaces(this.accountId, this.selected)
  .then(n=>{
    this.progressBar.setSuccess();
    this.alertService.success('Saved to collection');
    this.progressBar.completeLoading();
    // alert(`Saved to collection`);
  })
  
  // .3 second delay
  setTimeout(() => {
  // refreshes page without removing auth token in local storage
  this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
    this.router.navigate(['/collection',this.accountId])
  })}, 300);

  } else {
    this.alertService.danger('None selected to save');
    this.progressBar.completeLoading();
  }
  
  
  }

  goToMap(name: string, lat: any, long: any){
    const n = name;
    const queryParams: Params = 
    {locationParam: name + '|' + lat + '|' + long + '|' + this.query + '|' + this.accountId};
    console.log(">>> locationParam", queryParams);
    this.router.navigate(['/map', n], {queryParams : queryParams})
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }

  back(){
    console.log("Return to View 0")
    this.router.navigate(['/'])
  }

  login(){
    console.log("Go to login view")
    this.router.navigate(['/login'])
  }
}
