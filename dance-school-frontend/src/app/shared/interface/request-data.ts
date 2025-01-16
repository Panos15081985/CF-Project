import { ContactDetails, Guardians } from "./data";

export interface ReCourse {
    name:string,
    description:string,
    day:string,
    startTime:string,
    endTime:string,
    coachUuid:string
}

export interface ReDancer{
    firstname:string,
    lastname:string,
    gender:string,
    CantactDetailsInsertDTO:ContactDetails,
    gruardians:Guardians,
    courseIds:[]
}

export interface NewCredentials{
    username:string,
    password:string,
    uuid:string
}

export interface ReCoach{
    firstname:string,
    lastname:string,
    username:string,
    password:string,
    role:string,
    contactDetailsInsertDTO:{
        city:string,
        road_number: string | null;
        road: string | null;
        postalCode: string | null;
        email:string,
        phoneNumber:string
    }
}

export interface UpCoach{
    firstname:string,
    lastname:string,
    role:string,
    uuid:string,
    contactDetailsInsertDTO:{
        city:string,
        road_number: string | null;
        road: string | null;
        postalCode: string | null;
        email:string,
        phoneNumber:string
    }
}

export interface UpCourse{
    id:number,
    name:string,
    description:string,
    day:string,
    startTime:string,
    endTime:string,
    coachUuid:string
}

export interface LoggedInUser{
    username:string,
    role:string,
    uuid:string
   
}

export interface LoginCredentials{
    username: string,
    password: string
}

export interface TokenPayload {
    sub: string;   
    role: string;
    uuid: string;  
};

export interface CourseUpdateData {
    id: string;
    name: string;
    description: string;
    day: string;
    startTime: string;
    endTime: string;
    coachUuid: string;
}