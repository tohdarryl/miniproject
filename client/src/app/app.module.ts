import { NgModule, isDevMode, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { ServiceWorkerModule } from '@angular/service-worker';
import { SearchComponent } from './components/search.component';
import { ListComponent } from './components/list.component';
import { MaterialModule } from './material.module';
import { RegisterComponent } from './components/register.component';
import { LoginComponent } from './components/login.component';
import { HttpInterceptorService } from './services/http-interceptor.service';
import { SharedModule } from './shared/shared.module';
import { ForgotPasswordComponent } from './components/forgot-password.component';
import { CollectionComponent } from './components/collection.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { MapComponent } from './components/map.component';
import { ReviewComponent } from './components/review.component';
import { UserListComponent } from './components/user-list.component';
import { ChangePasswordComponent } from './components/change-password.component'




@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    ListComponent,
    RegisterComponent,
    LoginComponent,
    ForgotPasswordComponent,
    CollectionComponent,
    MapComponent,
    ReviewComponent,
    UserListComponent,
    ChangePasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialModule,
    SharedModule,
    GoogleMapsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: !isDevMode(),
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    })
  ],
  providers: [ {
    provide: HTTP_INTERCEPTORS,
    useClass: HttpInterceptorService,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
