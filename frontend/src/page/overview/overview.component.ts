import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CurrencyFrameService} from "../../service/currency-frame.service";
import {CurrencyFrame} from "../../model/CurrencyFrame";

@Component({
  selector: 'overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  currencyFrames: CurrencyFrame[]

  constructor(private router: Router, private currencyFrameService: CurrencyFrameService) {
    this.currencyFrames = [];
  }

  ngOnInit(): void {
    this.currencyFrameService.getLatestCurrencyFrames()
      .subscribe(sources => this.currencyFrames = sources)
  }

  openDetailsPage(source: string, country: string, firstCurrency: string, secondCurrency: string) {
    this.router.navigate([`/details`], {
      queryParams: {
        source: source,
        country: country,
        firstCurrency: firstCurrency,
        secondCurrency: secondCurrency
      }
    })
  }
}
