import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from './auth.service';  // Assure-toi que ce chemin est correct
import { User } from '../models/User';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  public isLogged = false;
  public user!: User;
  
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  constructor(private authService: AuthService) {}  // Injection du AuthService

  public getIsLoggedObservable(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(user: User, token: string): void {
    this.user = user;
    this.isLogged = true;
    localStorage.setItem('token', token);
    //console.log(localStorage.getItem('token'));
    this.next();
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }

  // Nouvelle méthode pour restaurer la session depuis un token stocké
  public restoreSessionFromToken(token: string): void {
    // Appel à AuthService pour obtenir l'utilisateur à partir du token
    this.authService.getCurrentUser().subscribe((user: User) => {
      this.logIn(user, token);  // Restaurer la session
    });
  }
}

