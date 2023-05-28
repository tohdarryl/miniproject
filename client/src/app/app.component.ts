import { Component, OnInit, Renderer2 } from '@angular/core';
import { AccountService } from './services/account.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { PlaceService } from './services/place.service';

declare global {
  interface Window { apiKey: string; }
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'client';
  helper = new JwtHelperService();
  key = '';

  
  

  constructor(private accSvc: AccountService, private placeSvc: PlaceService) { }

  async ngOnInit(): Promise<void> {
    const token = this.accSvc.getToken();
    this.accSvc.decodedToken = this.helper.decodeToken(token);

  
    const result = await (async () => {
      return await this.placeSvc.getApiKey();
    })();

    this.key = result.apiKey;
    // console.log(this.key)
    
    
    window.apiKey = this.key;
    // console.log("window.apiKey >>> ", window.apiKey)
    console.log("apiKey received from server")
    
    
  }
}
