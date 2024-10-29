// src/app/app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { NewsService } from './services/news.service';
import { AppRoutingModule } from './app-routing.module';
import { NewsComponent } from './news/news.component';

@NgModule({
  declarations: [AppComponent, NewsComponent],
  imports: [BrowserModule, HttpClientModule, FormsModule,AppRoutingModule],
  providers: [NewsService],
  bootstrap: [AppComponent]
})
export class AppModule {}
