import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { QuestionRoutingModule } from './question-routing.module';
import { QuestionCreateComponent } from './question-create/question-create.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuestionListComponent } from './question-list/question-list.component';
import { TagInputModule } from 'ngx-chips';
import { QuestionSelectComponent } from './question-select/question-select.component';

@NgModule({
  imports: [
    CommonModule,
    QuestionRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    TagInputModule
  ],
  declarations: [QuestionCreateComponent, QuestionListComponent, QuestionSelectComponent]
})
export class QuestionModule { }
