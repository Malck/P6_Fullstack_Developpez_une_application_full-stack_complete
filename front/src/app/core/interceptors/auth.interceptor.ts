import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept<T>(req: HttpRequest<T>, next: HttpHandler): Observable<HttpEvent<T>> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().append(
      'Authorization',
      'Bearer ' + token
    );

    const modifiedReq = req.clone({ headers });
    
    return next.handle(modifiedReq);
  }
}  
