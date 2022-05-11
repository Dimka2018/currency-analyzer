import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Article} from "../model/Article";

@Injectable({providedIn: 'root'})
export class NewsService {

  constructor(private http: HttpClient) {}

  public getNews(dates: string[]): Observable<Article[]> {
    return this.http.get<Article[]>("/api/news", {
      params: {
        dates: dates
      }
    })
  }

}
