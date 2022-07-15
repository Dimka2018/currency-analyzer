import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {CurrencyFrameService} from "../../service/currency-frame.service";

@Component({
  selector: 'admin-console',
  templateUrl: './admin-console.component.html',
  styleUrls: ['./admin-console.component.scss']
})
export class AdminConsole {

  constructor(private router: Router, private userService: UserService, private fxRateService: CurrencyFrameService) {
  }

  downloadHistory() {
    this.fxRateService.downloadHistory();
  }

  applyHistory(event: any) {
    this.fxRateService.applyHistory(event.target.files[0])
      .subscribe()
  }

  logout() {
    this.userService.logout()
      .subscribe(() => this.router.navigate(['/overview']))
  }

}
