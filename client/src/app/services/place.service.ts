import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { last, lastValueFrom, Observable } from 'rxjs';

import { Place } from '../models/place';

@Injectable({
  providedIn: 'root'
})
export class PlaceService {

  constructor(private http: HttpClient) { }

  // View 1: Show all place details
  // Param name 'input' must be the same as @RequestParam in Controller e.g. String input (for value to be passed to server)
  getPlaces(query: string): Promise<any> {
    const params = new HttpParams()
        .set("input", query);
    

    // For this particular case, if you dont set then it will be blank
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    return lastValueFrom(
      // this.http.get<Place[]>('api/search', {params: params})
      this.http.get<Place[]>('/place/' + 'api/search', {params: params, headers: headers})
    );
  }

  savePlaces(accountId:String, places:any[]) : Promise<any>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("save places for: "+accountId);
    console.log("Places include: "+places)
    

    return lastValueFrom(this.http.post<any>('/place/' + 'collection/' + accountId, places, {headers: headers}));
  }

  getPlacesForCollection(accountId: String) : Promise<any>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("get places for: "+accountId);
    return lastValueFrom(this.http.get<Place[]>('/place/' + 'collection/' + accountId, {headers: headers}));
  }

  deleteFromCollection(linkId: String) : Promise<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("delete: "+linkId);
    return lastValueFrom(this.http.delete<any>('/place/' + 'delete/' + linkId, {headers: headers}));
  }

  async getApiKey(): Promise<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', responseType: 'text' as 'json' });
    return lastValueFrom(this.http.get<any>('/place/' + 'apiKey', {headers: headers}));
  }


}
