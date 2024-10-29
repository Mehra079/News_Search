// src/app/app.component.ts
import { Component, ViewChild } from '@angular/core';
import { NewsComponent } from './news/news.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  keyword: string = '';
  interval: number = 12;
  intervalUnit: string = 'hours';
  offlineMode: boolean = false;

  @ViewChild(NewsComponent) newsComponent!: NewsComponent;

  searchNews(): void {
    // Update newsComponent properties before search
    if (this.newsComponent) {
      this.newsComponent.keyword = this.keyword;
      this.newsComponent.interval = this.interval;
      this.newsComponent.intervalUnit = this.intervalUnit;
      this.newsComponent.offlineMode = this.offlineMode;
      
      console.log('Offline Mode:', this.offlineMode); // Confirm toggle value in console

      this.newsComponent.fetchNews();
    }
  }

  toggleOfflineMode(): void {
    this.offlineMode = !this.offlineMode;  // Toggle offlineMode value
  }
}
