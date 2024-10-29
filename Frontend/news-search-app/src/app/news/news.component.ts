// src/app/components/news/news.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { IntervalGroup, NewsService } from '../services/news.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {
  @Input() keyword: string = '';
  @Input() interval: number = 12;
  @Input() intervalUnit: string = 'hours';
  @Input() offlineMode: boolean = false;

  intervalGroups: IntervalGroup[] = [];

  constructor(private newsService: NewsService) {}

  ngOnInit(): void {
    this.fetchNews();
  }

  fetchNews(): void {
    this.newsService.getNews(this.keyword, this.interval, this.intervalUnit, this.offlineMode).subscribe(data => {
      this.intervalGroups = data.intervalGroups;
    });
  }
}
