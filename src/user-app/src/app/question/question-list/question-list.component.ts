import { Component, OnInit } from '@angular/core';
import { Question } from "../question";
import { QuestionService } from "../question.service";
import { Router } from '@angular/router';



@Component({
  selector: 'app-question-list',
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.css'],
  providers: [QuestionService]
})

export class QuestionListComponent implements OnInit {

  private questions: Question[];

  constructor(private router: Router,private questionService: QuestionService) { }

  ngOnInit() {
    this.getAllQuestions();
  }

  getAllQuestions(){
    this.questionService.findAll().subscribe(
      questions => {
        this.questions = questions;
        },
        err => {
          console.log(err);
        }
    );
  }


  redirectNewQuestionPage() {
    this.router.navigate(['/questions/create']);
  }



  deleteQuestion(question: Question) {
    if(question){
      this.questionService.deleteQuestionById(question.id).subscribe(
        res => {
          this.getAllQuestions();
          this.router.navigate(['/questions']);
          console.log('Question deleted');
        }
      );
    }
  }

  QuestionPage(question: Question) {
    if (question) {
      this.router.navigate(['/question', question.id]);
    }
  }

}

