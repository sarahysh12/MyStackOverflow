import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Answer} from "../answer";
import { AnswerService } from "../answer.service";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-answer-select',
  templateUrl: './answer-select.component.html',
  styleUrls: ['./answer-select.component.css'],
  providers: [AnswerService]
})
export class AnswerSelectComponent implements OnInit {

  answerForm: FormGroup;
  private sub: any;

  questionId: string;
  id: string;
  content: string;
  answer: Answer;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private answerService: AnswerService){}

  ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
      this.questionId = params['qid'];
      this.content = params['ans'];
    });


    this.answerForm = new FormGroup({
      content: new FormControl('', Validators.required)
    });

    this.answerForm.patchValue({
      content: this.content
    });

  }

  onSubmit() {
    if (this.answerForm.valid) {
      if(this.id){
        let answer: Answer = new Answer(this.id,
          [this.answerForm.controls['content'].value].toString());
        this.answerService.updateAnswer(answer).subscribe();

      }
      this.answerForm.reset();
      this.router.navigate(['/question', this.questionId]);
    }
  }


}
