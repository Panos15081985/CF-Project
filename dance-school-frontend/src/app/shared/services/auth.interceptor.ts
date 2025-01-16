import { 

  HttpInterceptor,
  HttpRequest,

  HttpHandler
} from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
     intercept(req: HttpRequest<any>, next: HttpHandler){
          const token = localStorage.getItem("access_token")

          if(!token){
               return next.handle(req)
          }

          const authRequest = req.clone({
               headers: req.headers.set("Authorization", "Bearer " + token)
               
          })

          return next.handle(authRequest)
     }

}
