import {Component, OnInit} from '@angular/core';
import {User} from "../../model/User";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class Login {

  user: User = new User();

  constructor(private userService: UserService, private router: Router) {
  }

  login() {
    this.userService.login(this.user)
      .subscribe(() => this.router.navigate([`/admin-console`]));
  }
}
