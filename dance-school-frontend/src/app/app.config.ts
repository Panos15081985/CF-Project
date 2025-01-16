import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideHttpClient,withInterceptorsFromDi,HTTP_INTERCEPTORS  } from '@angular/common/http';
import { AuthInterceptorService } from './shared/services/auth.interceptor';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
     providers: [provideZoneChangeDetection({ eventCoalescing: true }), 
     provideRouter(routes), 
     provideAnimationsAsync(),
     provideHttpClient(withInterceptorsFromDi()),
     {
       provide: HTTP_INTERCEPTORS,
       useClass: AuthInterceptorService,
       multi:true
     }
     ]
   };





