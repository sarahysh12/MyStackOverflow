import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { QuestionService } from "../question.service";
import { AnswerService } from "../answer.service";
import { Question} from "../question";
import { Answer } from "../answer";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-question-select',
  templateUrl: './question-select.component.html',
  styleUrls: ['./question-select.component.css'],
  providers: [QuestionService, AnswerService]

})
export class QuestionSelectComponent implements OnInit {

  id: string;
  question: Question;

  answerForm: FormGroup;
  private sub: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private questionService: QuestionService,
              private answerService: AnswerService){}

  ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
    });
      this.getQuestion();
  }

  getQuestion(){
    this.questionService.findById(this.id).subscribe(
      question => {
        this.question = question;
        }
        ,error => {
        console.log(error);
      }
    );

  }

  editQuestionPage(question: Question) {
    if (question) {
      this.router.navigate(['/questions/edit', question.id]);
    }
  }

  onSubmit() {
    let answer: Answer = new Answer(null,this.answerForm.controls['content'].value);
    this.answerService.saveAnswer(answer).subscribe();
  }


}
