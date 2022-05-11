export class CurrencyFrame {

  source: string;
  country: string;
  firstCurrency: string;
  secondCurrency: string;
  buyPrice: number;
  sellPrice: number;
  spread: number;
  criticalMovingAverage: number;
  critical: boolean;
  date: string


  constructor(source: string, country: string, firstCurrency: string, secondCurrency: string, buyPrice: number, sellPrice: number,
              spread: number, criticalMovingAverage: number, critical: boolean, date: string) {
    this.source = source;
    this.country = country;
    this.firstCurrency = firstCurrency;
    this.secondCurrency = secondCurrency;
    this.buyPrice = buyPrice;
    this.sellPrice = sellPrice;
    this.spread = spread;
    this.criticalMovingAverage = criticalMovingAverage;
    this.critical = critical;
    this.date = date;
  }
}
