import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { async } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Subscription } from 'rxjs';
import { Place } from '../models/place';
import { Review } from '../models/review';
import { AccountService } from '../services/account.service';
import { PlaceService } from '../services/place.service';
import { ReviewService } from '../services/review.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-collection',
  templateUrl: './collection.component.html',
  styleUrls: ['./collection.component.css']
})
export class CollectionComponent implements OnInit, OnDestroy {
  
  accountId = ''
  query = ''
  linkId = ''
  param$!: Subscription;
  places!: Place[]
  reviews!: Review[]
  selectedFilter: string = '';

  @ViewChild('dialogRef')
  dialogRef!: TemplateRef<any>


  constructor(private activatedRoute: ActivatedRoute, private placeSvc: PlaceService,
    private router: Router, public accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService,
    private reviewSvc: ReviewService, public dialog: MatDialog) { }
  
  ngOnInit(): void {
    console.log("collection component")
    // to get value from /collection/<accountId> from list.component.ts
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        // app-routing.module.ts;  path: "collection/:accountId", component: CollectionComponent
        this.accountId = params['accountId']
        console.log(">>> query: ", this.accountId)
        const l = await this.placeSvc.getPlacesForCollection(this.accountId)
        // console.log(l)
      

        // if (l == undefined || l.length == 0) {
        //   // route back to search.component if result is undefined or empty
        //   this.router.navigate(['/'])
        // } else {
          // set as result as Place[] if ok
          this.places = l
          this.places.forEach(async (place) => {
            const r = await this.reviewSvc.getReviews(this.accountId+'+'+place.placeId)
            // console.log(r)
            place.review = r
          })
       

          
        // }
      }
    )
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }

  goToMap(name: string, lat: any, long: any){
    const n = name;
    const queryParams: Params = 
    {locationParam: name + '|' + lat + '|' + long + '|' + this.query + '|' + this.accountId};
    console.log(">>> locationParam", queryParams);
    this.router.navigate(['/map', n], {queryParams : queryParams})
  }

  delete(placeId: String){
    this.progressBar.startLoading();
    this.linkId = this.accountId + '+' + placeId
    console.log(this.linkId)
    this.placeSvc.deleteFromCollection(this.linkId);
    this.reviewSvc.deleteReviews(this.linkId)
    this.progressBar.setSuccess();
    this.alertService.success('Deleted Successfully');
    this.progressBar.completeLoading(); 


    // .3 second delay
  setTimeout(() => {
    // refreshes page without removing auth token in local storage
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['/collection',this.accountId])
    })}, 300);
   
  
  } 

  addReview(placeId:String) {
    this.linkId = this.accountId + '+' + placeId
    console.log(this.linkId)
    this.router.navigate(['/review',this.linkId])
  }

  openTempDialog(){
    const myCompDialog = this.dialog.open(this.dialogRef, {data:this.places, panelClass:'fullscreen-dialog', height: '100vh',
  width: '100%'})

  }

  applyFilter(filter: string) {
    this.selectedFilter = filter;
  }

}
