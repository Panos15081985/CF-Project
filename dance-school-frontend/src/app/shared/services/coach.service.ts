import { effect, inject, Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { CourseFilters, PagResponse } from '../interface/menu-entry';
import { Coach, CourseDelete, LoginResponse, ValiddatedCredential } from '../interface/response-data';
import { LoggedInUser, LoginCredentials, NewCredentials, ReCoach, TokenPayload, UpCoach } from '../interface/request-data';
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';

const API_URL = environment.apiURL;

@Injectable({
  providedIn: 'root'
})
export class CoachService {
  http: HttpClient = inject(HttpClient)
  router = inject(Router)
  user = signal<LoggedInUser | null>(null)
 
     constructor() { 
          const access_token = localStorage.getItem("access_token")
          if(access_token){
               const decodeTokenSubject = jwtDecode<TokenPayload>(access_token)
               
               this.user.set({
                    username: decodeTokenSubject.sub,
                    role: decodeTokenSubject.role,
                    uuid:decodeTokenSubject.uuid
               })
          }
          effect(()=>{
               if(!this.user()){ console.log("Not LogedIn User") }
          })
     }

     hasRole(role: string): boolean {
          return this.user()?.role === role
     }

     hasAnyRole(roles: string[]): boolean {
          const coachRole = this.user()?.role;
          return coachRole ? roles.includes(coachRole) : false;
     }

     loginUser(credentials: LoginCredentials){
          return this.http.post<LoginResponse>(API_URL + "/api/authenticate", credentials)
     }

     validCredential(oldCredentials: LoginCredentials){
          return this.http.post<ValiddatedCredential>(API_URL + "/api/authenticationChangeUsernamePassword", oldCredentials)
     }

     newCredentials(newCredentials: NewCredentials): Observable<any> {
          return this.http.post<CourseDelete>(API_URL + "/coach/update/credentials", newCredentials);
     }


     getFilteredCoaches(filters: any) {
          return this.http.post<PagResponse>(API_URL + "/coach/filtered", filters, {
               headers: {
               'Content-Type': 'application/json',
               'Accept': 'application/json',
               },
          });
     }

     deleteCoach(uuid: string) {
          return this.http.delete<CourseDelete>(API_URL +"/coach/delete/" + uuid);
     }

     saveCoach(data:ReCoach){
          return this.http.post(API_URL + "/coach/save", data)
     }

     updateCoach(updateData: UpCoach) {
          return this.http.post(API_URL + "/coach/update", updateData);
     }

     logoutUser(){
          this.user.set(null)
          localStorage.removeItem("access_token")
     }

     getAllCoaches(){
          return this.http.get<Coach[]>(API_URL + "/coach/getAllCoaches")
     }

}
