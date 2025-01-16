import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { CourseFilters, PagResponse } from '../interface/menu-entry';
import { CoachUuid, CourseDelete,Course } from '../interface/response-data';
import { ReCourse, UpCourse } from '../interface/request-data';
import { FormArray } from '@angular/forms';

const API_URL = environment.apiURL;

@Injectable({
     providedIn: 'root'
})
export class CourseService {

     http: HttpClient = inject(HttpClient)
     router = inject(Router)

     constructor() { }

     saveCourse(data: ReCourse) {
          return this.http.post<CoachUuid>(`${API_URL}/course/save`, data);
     }

     getFilteredCourses(filters: any) {
          return this.http.post<PagResponse>(API_URL + "/course/paginated", filters, {
               headers: {
               'Content-Type': 'application/json',
               'Accept': 'application/json',
               },
          });
     }

     getAllCourses(){
          return this.http.get<Course[]>(API_URL + "/course/getAllCourses")
     }

     deleteCourse(id: number) {
          return this.http.delete<CourseDelete>(API_URL +"/course/delete/" + id);
     }

     updateCourse(data: UpCourse) {
          return this.http.post(API_URL + "/course/update", data);
     }
}
