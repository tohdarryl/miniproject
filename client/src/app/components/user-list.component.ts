import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from '@full-fledged/alerts';
import { Subscription } from 'rxjs';
import { Account } from '../models/account';
import { AccountService } from '../services/account.service';
import { PlaceService } from '../services/place.service';
import { ProgressBarService } from '../shared/services/progress-bar.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit, OnDestroy{

  query = ""
  param$!: Subscription;
  users!: Account[]

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
        const l = await this.accSvc.getUsers(this.query)

        console.log(l)

        // if (l == undefined || l.length == 0) {
        //   // route back to search.component if result is undefined or empty
        //   this.router.navigate(['/'])
        // } else {
        //   // set as result as Account[] if ok
          this.users = l
        // }
      }
    )
  }

  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }


}
