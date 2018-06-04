import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { QuestionCreateComponent } from './question-create/question-create.component';
import { QuestionListComponent } from './question-list/question-list.component';
import { QuestionSelectComponent } from './question-select/question-select.component';
import { AnswerSelectComponent } from './answer-select/answer-select.component';

const routes: Routes = [
{path: 'questions/create', component: QuestionCreateComponent},
{path: 'questions', component: QuestionListComponent},
{path: 'questions/edit/:id', component: QuestionCreateComponent},
{path: 'question/:id', component: QuestionSelectComponent},
{path: 'answer/edit/:id/:ans/:qid', component: AnswerSelectComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QuestionRoutingModule { }