import { Component, OnInit, Type } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from './core/services/auth.service';
import { SessionService } from './core/services/session.service';
import { User } from './core/models/User';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './auth/components/login/login.component';
import { RegisterComponent } from './auth/components/register/register.component';

type AppComponents = HomeComponent | LoginComponent | RegisterComponent;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'front';

  headerType!: HeaderType;
  HeaderTypeEnum = HeaderType;

  showHeader = true;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private sessionService: SessionService
  ) {}
  

  ngOnInit() {
    // Restaurer la session au démarrage de l'application
    this.restoreSession();

    // Gérer les événements de navigation pour mettre à jour l'en-tête
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        const activeComponent = this.getActiveComponent(this.activatedRoute);
        if (activeComponent === HomeComponent) {
          this.headerType = HeaderType.HomeHeader;
        } else if (
          activeComponent === LoginComponent ||
          activeComponent === RegisterComponent
        ) {
          this.headerType = HeaderType.LoginHeader;
        } else {
          this.headerType = HeaderType.MddHeader;
        }
      });
  }

  // Fonction pour restaurer la session depuis le token
  private restoreSession(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.authService.getCurrentUser().subscribe(
        (user: User) => {
          this.sessionService.logIn(user, token); // Restaurer la session avec l'utilisateur et le token
        },
        (error) => {
          // Gestion des erreurs si la restauration de l'utilisateur échoue
          console.error('Failed to restore session', error);
          this.sessionService.logOut(); // Déconnecter si l'utilisateur ne peut pas être récupéré
        }
      );
    }
  }

  // Récupère le composant actif associé à la route donnée
  private getActiveComponent(route: ActivatedRoute): Type<AppComponents> | null {
  if (route.routeConfig && route.routeConfig.component) {
    return route.routeConfig.component as Type<AppComponents>;
  } else if (route.firstChild) {
    return this.getActiveComponent(route.firstChild);
  }
  return null;
}
  
}

export enum HeaderType {
  HomeHeader,
  LoginHeader,
  MddHeader,
}