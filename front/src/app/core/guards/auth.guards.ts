import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { SessionService } from '../services/session.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private sessionService: SessionService, private router: Router) {}

  canActivate(): Observable<boolean> {
    const token = localStorage.getItem('token');  // Vérifier si le token existe dans le localStorage

    if (token && !this.sessionService.isLogged) {
      // Restaurer la session à partir du token
      this.sessionService.restoreSessionFromToken(token);
    }

    return this.sessionService.getIsLoggedObservable().pipe(
      map((isLogged) => {
        if (!isLogged) {
          this.router.navigate(['login']);
          return false;
        }
        return true;
      })
    );
  }
}


