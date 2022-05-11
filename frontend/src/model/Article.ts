export class Article {

  source: string;
  url: string;
  title: string;
  date: string;

  constructor(source: string, url: string, title: string, date: string) {
    this.source = source;
    this.url = url;
    this.title = title;
    this.date = date;
  }
}
