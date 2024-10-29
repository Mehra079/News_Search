// src/app/services/news.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NewsArticle {
  title: string;
  description: string;
  url: string;
  publishedAt: string;
}

export interface IntervalGroup {
  interval: string;
  count: number;
  articles: NewsArticle[];
}

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private apiUrl = 'http://localhost:8080/api/news/search';

  constructor(private http: HttpClient) {}

  getNews(keyword: string, interval: number, intervalUnit: string, offlineMode: boolean): Observable<{ intervalGroups: IntervalGroup[] }> {
    return this.http.get<{ intervalGroups: IntervalGroup[] }>(
      `${this.apiUrl}?keyword=${keyword}&interval=${interval}&intervalUnit=${intervalUnit}&offlineMode=${offlineMode}`
    );
  }
}
