import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { Review } from '../models/review';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }

   // View 2: Get reviews for individual place
   getReviews(linkId: string): Promise<Review[]>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("get all comments !");
    return lastValueFrom(this.http
        .get<Review[]>('/place/' +'reviews/' + linkId, {headers: headers}));
  
  }
  // View 3: To insert review for individual place
  saveReview(r:Review) : Promise<any>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    const body=JSON.stringify(r);
    console.log("save Review !");
    return lastValueFrom(this.http.post<Review>('/place/' + r.id, body, {headers: headers}));
  }

  deleteReviews(linkId: string) : Promise<any>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("delete all comments when place is unsaved !");
    return lastValueFrom(this.http
      .delete<any>('/place/' + 'delete/reviews/' + linkId, {headers: headers}));
  }
}
