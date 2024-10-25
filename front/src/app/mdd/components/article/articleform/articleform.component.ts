import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ArticlesService } from '../../../services/articles.service';
import { Router } from '@angular/router';
import { Subject as MySubject } from '../../../../core/models/Subject';
import { SubjectService } from 'src/app/core/services/subject.service';
import { SessionService } from 'src/app/core/services/session.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-articleform',
  templateUrl: './articleform.component.html',
  styleUrls: ['./articleform.component.scss'],
})
export class ArticleformComponent implements OnInit, OnDestroy {
  themes: MySubject[] = [];
  selectedTheme!: string;
  message: string | null = null;
  errorMessage: string | null = null;

  private unsubscribe$ = new Subject<void>(); // Subject pour désabonnement

  constructor(
    private articleService: ArticlesService,
    private router: Router,
    private subjectService: SubjectService,
    private sessionService: SessionService
  ) {}

  ngOnInit() {
    this.subjectService.getSubjects()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe({
        next: (subjects) => {
          this.themes = subjects;
        },
        error: (error) => {
          console.error('Failed to load subjects', error);
        },
      });
  }

  handleSuccess(message: string) {
    this.message = message;
    this.router.navigateByUrl('/mdd/article');
  }

  handleError(message: string) {
    console.error(message);
    this.errorMessage = message;
  }

  onCreateArticle(form: NgForm) {
    if (form.valid) {
      const articleData = {
        userId: this.sessionService.user.id.toString(),
        subjectId: form.value.theme,
        title: form.value.title,
        content: form.value.content,
      };

      this.articleService.createArticle(articleData)
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe({
          next: (message) => {
            this.handleSuccess('Article created successfully');
          },
          error: () => {
            this.handleError('Failed to create Article');
          },
        });
    }
  }

  ngOnDestroy() {
    this.unsubscribe$.next(); // Notifier les abonnements de se désabonner
    this.unsubscribe$.complete(); // Compléter le Subject
  }
}
