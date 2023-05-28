import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Subscription } from 'rxjs';
import { Review } from '../models/review';
import { AccountService } from '../services/account.service';
import { PlaceService } from '../services/place.service';
import { ReviewService } from '../services/review.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit, OnDestroy{
  form!:FormGroup
  linkId = ''
  param$!: Subscription;

  constructor(private activatedRoute: ActivatedRoute, private placeSvc: PlaceService,
    private router: Router, public accSvc: AccountService,
    public progressBar: ProgressBarService, private alertService: AlertService,
    private reviewSvc: ReviewService, private fb: FormBuilder) { }


  ngOnInit(): void {
    this.form = this.fb.group({
      comment: this.fb.control<string>('')
    })
    console.log("review component")
    // to get value from /review/<linkId> from Collection.component.ts
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        // app-routing.module.ts;  path: "review/:linkId", component: ReviewComponent
        this.linkId = params['linkId']
        console.log(">>> query: ", this.linkId)
       
      }
    )
  }  

  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }
  

  // save comment to MongoDB
saveComment(){
  const commentFormVal = this.form?.value['comment'];
  // instantiate a new Comment interface
  const r = {} as Review;
  r.comment = commentFormVal;
  r.id = this.linkId;

  this.reviewSvc.saveReview(r);

   // .2 second delay
   setTimeout(() => {
    // refreshes page without removing auth token in local storage
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate(['/collection', this.accSvc.decodedToken.accountId]);
    })}, 200);
 
}

cancel(){
  this.router.navigate(['/collection', this.accSvc.decodedToken.accountId]);
}

}
