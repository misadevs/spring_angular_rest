import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_SERVER = "http://localhost:8080/api/v1/usuario/";

  constructor(private httpClient: HttpClient) { }
  
  public getAllUsers(): Observable<any>{
    return this.httpClient.get(this.API_SERVER);
  }

  public createUser (user:any): Observable<any>{
    return this.httpClient.post(this.API_SERVER,user);
  }

  public deleteUser(id: string):Observable<any>{
    return this.httpClient.delete(this.API_SERVER+id);
  }

  public updateUser(user:any):Observable<any>{
    return this.httpClient.put(this.API_SERVER+user.id,user);
  }
}
