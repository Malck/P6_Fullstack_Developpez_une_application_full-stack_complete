import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, filter, map, takeUntil, tap } from 'rxjs';
import { Subject as MySubject } from 'src/app/core/models/Subject';
import { User } from 'src/app/core/models/User';
import { AuthService } from 'src/app/core/services/auth.service';
import { SessionService } from 'src/app/core/services/session.service';
import { SubjectService } from 'src/app/core/services/subject.service';
import { SubscriptionService } from 'src/app/core/services/subscriptionService';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
})
export class MeComponent implements OnInit, OnDestroy {
  profileForm!: FormGroup;
  subjects: MySubject[] = [];
  errorMessage: string | null = null;
  message: string | null = null; // Message de succès
  currentUser!: User;

  private unsubscribe$: Subject<boolean> = new Subject<boolean>();

  constructor(
    private subjectService: SubjectService,
    private subscriptionService: SubscriptionService,
    private sessionService: SessionService,
    private router: Router,
    private formeBuilder: FormBuilder,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Chargement des sujets non abonnés
    this.getUnsubscribedSubjects();

    // Initialisation des informations utilisateur
    this.currentUser = this.sessionService.user;

    // Création du formulaire avec les données de l'utilisateur actuel
    this.profileForm = this.formeBuilder.group({
      username: [this.currentUser.username, [Validators.required]],
      email: [this.currentUser.email, [Validators.required, Validators.email]],
    });
  }

  // Méthode appelée lors de la sauvegarde des modifications du profil
  onSave() {
    if (this.profileForm.valid) {
      this.authService
        .updateUser(this.currentUser.id.toString(), this.profileForm.value)
        .pipe()
        .subscribe({
          next: (message) => {
            this.handleSuccess('Les modifications ont été prises en compte', message.token);
          },
          error: (error) => {
            this.handleError('Échec de la mise à jour de l\'utilisateur');
          },
        });
    }
  }

  // Méthode pour gérer le succès de la mise à jour du profil
  handleSuccess(message: string, token: string) {
    // Sauvegardez le token dans le localStorage
    localStorage.setItem('token', token);
  
    // Affichez le message de succès
    this.message = message;
  
    // Mémoriser les nouvelles données de l'utilisateur
    this.authService.getCurrentUser().subscribe((user: User) => {
      this.sessionService.logIn(user, token); // Passer le token ici
    });
  
    // Effacer le message après 5 secondes
    setTimeout(() => {
      this.message = null;
    }, 5000); // 5000 millisecondes = 5 secondes
  }

  // Méthode pour gérer les erreurs lors de la mise à jour du profil
  handleError(message: string) {
    console.error(message);
    this.errorMessage = message;
  
    // Effacer le message après 5 secondes
    setTimeout(() => {
      this.errorMessage = null;
    }, 5000); // 5000 millisecondes = 5 secondes
  }

  // Méthode pour obtenir les sujets non abonnés
  getUnsubscribedSubjects(): void {
    this.subjectService
      .getSubjects()
      .pipe(
        map((subjects) => subjects.filter((subject) => subject.followed)),
        tap({
          next: (subjects) => {
            this.subjects = subjects;
          },
          error: (error) => {
            console.error(error);
            this.errorMessage = 'Erreur lors du chargement des sujets';
          },
        }),
        takeUntil(this.unsubscribe$)
      )
      .subscribe();
  }

  // Méthode pour gérer l'abonnement/désabonnement aux sujets
  onSubscribeSubject(subject: MySubject) {
    if (!subject) return; // S'assurer que le sujet existe

    const userId: number = this.currentUser.id;

    if (subject.followed) {
      this.subscriptionService
        .unsubscribeSubject(subject.id, userId)
        .subscribe(() => {
          subject.followed = false;
        });
    } else {
      this.subscriptionService
        .subscribeToSubject(subject.id, userId)
        .subscribe(() => {
          subject.followed = true;
        });
    }
  }

  // Méthode pour se déconnecter et rediriger vers la page d'accueil
  onLogoutClick() {
    this.sessionService.logOut();

    // Redirigez vers la page d'accueil
    this.router.navigateByUrl('/');
  }

  ngOnDestroy(): void {
    // Nettoyer les abonnements pour éviter les fuites de mémoire
    this.unsubscribe$.next(true);
    this.unsubscribe$.unsubscribe();
  }
}
