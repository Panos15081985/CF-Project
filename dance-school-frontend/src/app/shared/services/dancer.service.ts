import { Injectable, inject } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ReDancer } from '../interface/request-data';
import { CourseDelete, Dancer } from '../interface/response-data';
import { PagResponse } from '../interface/menu-entry';

const API_URL = environment.apiURL;

@Injectable({
  providedIn: 'root'
})
export class DancerService {

     http: HttpClient = inject(HttpClient)

     constructor() { }

     savedancer(data:ReDancer){
          return this.http.post(API_URL + "/dancer/save", data)
     }

     deleteDancer(uuid: string) {
         return this.http.delete<CourseDelete>(API_URL +"/dancer/delete" + uuid);
     }

     getFilteredDancers(filters: any) {
         return this.http.post<PagResponse>(API_URL + "/dancer/filtered", filters, {
               headers: {
               'Content-Type': 'application/json',
               'Accept': 'application/json',
               },
         });
     }

     updateDancer(updateData: Dancer) {
          return this.http.post<Dancer>(`${API_URL}/dancer/update`, updateData);
        }
}
