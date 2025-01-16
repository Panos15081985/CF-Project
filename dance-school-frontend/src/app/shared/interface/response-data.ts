import { ContactDetails, Guardians } from "./data"

export interface Course {
    
    coachUuid: string,
    day:string,
    description:string, 
    firstnameLastnameCoach:string,
    id:number
    name:string,
    role:string,
    startTime:string,
    endTime:string

}

export interface CoachUuid{
    coachUuid: string,
    coach:string
}

export interface Coach{
     id:number
     contactDetailsReadOnlyDTO: ContactDetails,
     courses:[Course],
     firstname:string,
     lastname:string,
     role:string,
     uuid:string
}

export interface CourseDelete{
    message: string,
    timeStamp: string
}

export interface ValiddatedCredential{
    message:string,
    accept:boolean,
    timeStamp:string
}

export interface LoginResponse {
    firstname: string;
    role: string;
    token: string;
}
export interface Dancer {
     id:number,
     uuid:string,
     firstname: string,
     lastname:string,
     contractEnd:string,
     dateOfBirth:string,
     gender:string,
     contactDetailsReadOnlyDTO: ContactDetails,
     guardianReadOnlyDTO: Guardians,
     courseList:[Course]
}