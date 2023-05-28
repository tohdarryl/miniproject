import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, OnDestroy {

  queryParams$!: Subscription
  locationParam!: any
  name = ''
  lat!: number
  long!: number
  query = ''
  accountId = ''

  //Maps 
  display: any;
  center!: google.maps.LatLngLiteral;
  zoom = 19;


  constructor(private activatedRoute: ActivatedRoute, private router: Router,
    public accSvc: AccountService) { }

  ngOnInit(): void {
    // subscribe to queryParams from previous list.component
    this.queryParams$ = this.activatedRoute.queryParams.subscribe(
      (queryParams) => {
        console.log(queryParams)
        this.locationParam = queryParams['locationParam'].split('|')
        console.log(this.locationParam[0]);
        console.log(this.locationParam[1]);
        console.log(this.locationParam[2]);
        console.log(this.locationParam[3]);
        console.log(this.locationParam[4]);
        this.name = this.locationParam[0];
        this.lat = this.locationParam[1];
        this.long = this.locationParam[2];
        this.query = this.locationParam[3];
        this.accountId = this.locationParam[4];
        this.center = {
          lat: +this.lat,
          lng: +this.long
        }
      }
    )
  }



   /*------------------------------------------
    --------------------------------------------
    moveMap()
    --------------------------------------------
    --------------------------------------------*/
    moveMap(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) 
      this.center = (event.latLng.toJSON());
  }

  /*------------------------------------------
  --------------------------------------------
  move()
  --------------------------------------------
  --------------------------------------------*/
  move(event: google.maps.MapMouseEvent) {
      if (event.latLng != null) 
      this.display = event.latLng.toJSON();
  }

  ngOnDestroy(): void {
    this.queryParams$.unsubscribe()
  }

  back(){
    // if query is an empty string, must use '===' instead of '=='
    if(this.query === ""){
      this.router.navigate(['/collection',this.accountId])
    } else {
      this.router.navigate(['/list', this.query]);
    
    }
  }

}
