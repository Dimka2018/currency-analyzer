import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CurrencyFrame} from "../model/CurrencyFrame";

@Injectable({providedIn: 'root'})
export class CurrencyFrameService {

  constructor(private http: HttpClient) {}

  public getLatestCurrencyFrames(): Observable<CurrencyFrame[]> {
    return this.http.get<CurrencyFrame[]>("/api/currencies/frames")
  }

  public getCurrencyFrames(source: string, country: string, firstCurrency: string, secondCurrency: string): Observable<CurrencyFrame[]> {
    return this.http.get<CurrencyFrame[]>("/api/currencies/frames/latest", {
      params: {
        source: source,
        country: country,
        firstCurrency: firstCurrency,
        secondCurrency: secondCurrency
      }
    })
  }

}
