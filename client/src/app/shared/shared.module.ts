import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ColumnOneComponent } from './layouts/column-one/column-one.component';
import { HeaderComponent } from './components/header/header.component';
import { MaterialModule } from '../material.module';
import { NgProgressModule } from 'ngx-progressbar';

// import for alerts
import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { AlertModule } from '@full-fledged/alerts';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    ColumnOneComponent,
    HeaderComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    NgProgressModule,
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    // Specify your library as an import
    AlertModule.forRoot({maxMessages: 5, timeout: 5000, positionX: 'right'}),
    BsDropdownModule.forRoot()
  ],
  exports: [
    ColumnOneComponent
  ]
})
export class SharedModule { }
