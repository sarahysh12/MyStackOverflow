import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { QuestionService } from "../question.service";
import { AnswerService } from "../answer.service";
import { CommentService } from "../comment.service";
import { Question} from "../question";
import { Answer } from "../answer";
import { Comment } from "../comment";
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-question-select',
  templateUrl: './question-select.component.html',
  styleUrls: ['./question-select.component.css'],
  providers: [QuestionService, AnswerService, CommentService]

})
export class QuestionSelectComponent implements OnInit {

  id: string;
  question: Question;
  comment: Comment;

  answerForm: FormGroup;
  commentForm: FormGroup;
  private sub: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private questionService: QuestionService,
              private answerService: AnswerService,
              private commentService: CommentService){}

  ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
      this.id = params['id'];
    });
      this.getQuestion();

    this.commentForm = new FormGroup({
      cmt: new FormControl('', Validators.required)
    });

    this.answerForm = new FormGroup({
      content: new FormControl('', Validators.required)
    });
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

  editAnswerPage(answer: Answer) {
    if (answer) {
      this.router.navigate(['/answer/edit', answer.id, answer.answer, this.id]);
    }

  }

  onSubmit() {
    let answer: Answer = new Answer(null,this.answerForm.controls['content'].value);
    this.answerService.saveAnswer(this.question.id, answer).subscribe(
     res => {
          this.getQuestion();
          this.answerForm.reset();
          this.router.navigate(['/question']);
        }
    );
  }

 deleteAnswer(answer: Answer) {
    if(answer){
      this.answerService.deleteAnswerById(answer.id).subscribe(
        res => {
          this.getQuestion();
          this.router.navigate(['/question']);
          console.log('Answer deleted');
        }
      );
    }
  }

 deleteComment(comment: Comment) {
    if(comment){
      this.commentService.deleteCommentById(comment.id).subscribe(
        res => {
          this.getQuestion();
          this.router.navigate(['/question']);
          console.log('Comment deleted');
        }
      );
    }
  }


  editComment(answer: Answer, comment: Comment){
    this.commentForm.patchValue({
    cmt: comment.content});
    this.comment = comment;
  }



   saveComment(answer: Answer) {
      if(this.comment){
        let cmt: Comment = new Comment(this.comment.id,this.commentForm.controls['cmt'].value);
        this.commentService.updateComment(answer.id, this.comment.id, cmt).subscribe(
        res => {
          this.getQuestion();
          this.commentForm.reset();
          this.router.navigate(['/question']);
        });
        this.comment = null;
      }
      else{
        let comment: Comment = new Comment(null,this.commentForm.controls['cmt'].value);
        this.commentService.saveComment(answer.id, comment).subscribe(
        res => {
          this.getQuestion();
          this.commentForm.reset();
          this.router.navigate(['/question']);
        });
    }
  }

}
