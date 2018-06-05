import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { QuestionService } from "../question.service";
import { Question} from "../question";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-question-create',
  templateUrl: './question-create.component.html',
  styleUrls: ['./question-create.component.css'],
  providers: [QuestionService]

})
export class QuestionCreateComponent implements OnInit {

  id: string;
  question: Question;

  questionForm: FormGroup;
  private sub: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private questionService: QuestionService){}

  ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
    });


    this.questionForm = new FormGroup({
      title: new FormControl('', Validators.required),
      content: new FormControl('', Validators.required),
      tags: new FormControl('', Validators.required)
    });


  if (this.id) {
    this.questionService.findById(this.id).subscribe(
      question => {
          this.id = question.id;
          this.questionForm.patchValue({
          title: question.title,
          content: question.content,
          tags: question.tags
        });
      },error => {
        console.log(error);
      }
    );

  }

  }


  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onSubmit() {
    if (this.questionForm.valid) {
      if(this.id){
        let question: Question = new Question(this.id,
        this.questionForm.controls['title'].value,
        this.questionForm.controls['content'].value,
        [this.questionForm.controls['tags'].value[0].value]);
        this.questionService.updateQuestion(question).subscribe();

     } else {
        let question: Question = new Question(null,
          this.questionForm.controls['title'].value,
          this.questionForm.controls['content'].value,
          [this.questionForm.controls['tags'].value[0].value]);
        this.questionService.saveQuestion(question).subscribe();

      }

      this.questionForm.reset();
      this.router.navigate(['/questions']);
    }
  }

  redirectQuestionPage() {
    this.router.navigate(['/questions']);

  }

}
