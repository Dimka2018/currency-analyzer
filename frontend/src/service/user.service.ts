import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/User";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class UserService {

  constructor(private http: HttpClient) {
  }

  public login(user: User): Observable<void> {
    let formData = new FormData();
    formData.append("username", user.username)
    formData.append("password", user.password)
    return this.http.post<void>("/api/login", formData);
  }

  public logout(): Observable<void> {
    return this.http.get<void>('/api/logout');
  }

}
