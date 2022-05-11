import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Color, LegendPosition, ScaleType} from '@swimlane/ngx-charts';
import {CurrencyFrameService} from "../../service/currency-frame.service";
import {CurrencyFrame} from "../../model/CurrencyFrame";
import {NewsService} from "../../service/news.service";
import {Article} from "../../model/Article";

@Component({
  selector: 'details-page',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {

  colorSchema: Color = {
    name: 'myScheme',
    selectable: true,
    group: ScaleType.Time,
    domain: ['#ff0000', '#000000'],
  };
  legendPosition = LegendPosition.Below;
  onlineFrames: CurrencyFrame[]
  actualOnlineFrame: CurrencyFrame;
  onlineFramesChart: any[]
  news: Article[];
  filteredNews: Article[];
  filterSource: string;
  filterDate: string;
  filterText: string;

  constructor(private route: ActivatedRoute, private frameService: CurrencyFrameService, private newsService: NewsService) {
    this.onlineFrames = [];
    this.onlineFramesChart = [];
    this.news = [];
    this.filteredNews = [];
    this.actualOnlineFrame = new CurrencyFrame('', '', '', '',
      0, 0, 0, 0, false, '')
    this.filterSource = '';
    this.filterDate = '';
    this.filterText = '';
  }

  ngOnInit(): void {
    let source = this.route.snapshot.queryParamMap.get('source')!.toString();
    let country = this.route.snapshot.queryParamMap.get('country')!.toString();
    let firstCurrency = this.route.snapshot.queryParamMap.get('firstCurrency')!.toString();
    let secondCurrency = this.route.snapshot.queryParamMap.get('secondCurrency')!.toString();

    this.frameService.getCurrencyFrames(source, country, firstCurrency, secondCurrency)
      .subscribe(frames => {
        this.onlineFrames = frames
        this.actualOnlineFrame = frames[frames.length - 1];
        this.onlineFramesChart = this.toChart(frames)
        let criticalDates: string[] = frames.filter(frame => frame.critical)
          .map(frame => frame.date)
          .map(date => `${date.substring(0, 10)}T00:00:00.000+00:00`);

        let dates = new Set(criticalDates.concat(criticalDates));

        this.newsService.getNews(Array.from(dates)).subscribe(news => {
          this.news = news;
          this.applyFilter();
        })
      })
  }

  toChart(frames: CurrencyFrame[]): any {
    let spreadSeries = frames.map(frame => this.toChartElement(frame.date, frame.spread));
    let movingAverageSeries = frames.map(frame => this.toChartElement(frame.date, frame.criticalMovingAverage));

    return [
      {name: "Critical", series: movingAverageSeries},
      {name: frames[0].source || '', series: spreadSeries}
    ]
  }

  toChartElement(name: string, value: number) {
    return {
      name: name,
      value: value
    }
  }

  openUrl(url: string) {
    // @ts-ignore
    window.open(url, '_blank').focus();
  }

  getNewsSources(): Set<string> {
    return new Set(this.news.map(article => article.source));
  }

  getNewsDates(): Set<string> {
    let dates = this.news.map(article => article.date)
      .map(date => date.substr(0, 10))
      .sort()
    return new Set(dates);
  }

  applyFilter() {
    let filteredNews: Article[] = JSON.parse(JSON.stringify(this.news));
    if (this.filterSource) {
      filteredNews = filteredNews.filter(article => article.source === this.filterSource)
    }
    if (this.filterDate) {
      filteredNews = filteredNews.filter(article => article.date.startsWith(this.filterDate))
    }
    if (this.filterText) {
      filteredNews = filteredNews.filter(article => article.title.toLowerCase().indexOf(this.filterText.toLowerCase()) !== -1)
    }
    this.filteredNews = filteredNews.sort((n1, n2) => n2.date.localeCompare(n1.date));
  }
}
