package me.arminb.sara.controller;

import me.arminb.sara.controller.models.QuestionRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Question;
import me.arminb.sara.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @RequestMapping(value="/questions/{id}", method = RequestMethod.GET)
    public ResponseEntity<Question> finQuestiondById(@PathVariable("id") String id) {
        try {
            Question question = questionService.findQuestionById(id);
            if(question != null) {
                return new ResponseEntity<Question>(question, HttpStatus.OK);
            } else {
                return new ResponseEntity<Question>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/questions",method = RequestMethod.GET)
    public  ResponseEntity<List<Question>> findQuestion( @RequestParam(value="title", required=false) String title,
                                             @RequestParam(value="tag", required = false) String tag,
                                             @RequestParam(value="page", required = false) Integer pageNumber,
                                             @RequestParam(value="pageCount", required = false) Integer pageCount) {
        try {
            List<Question> questions = questionService.findQuestion(title, tag, pageNumber, pageCount);
            if(questions != null) {
                return new ResponseEntity<List<Question>>(questions, HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Question>>(HttpStatus.NOT_FOUND);

            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<List<Question>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/questions",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionRequest questionReq) {
        try {
            Question question = questionReq.toQuestion();
            return new ResponseEntity<Question>(questionService.saveQuestion(question), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value= "questions/{id}", method = RequestMethod.PUT, consumes =  MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> updateQuestion(@PathVariable("id") String questionId, @RequestBody QuestionRequest questionReq) {
        try {
            Question question = questionReq.toQuestion();
            question.setId(questionId);
            Question questionResponse = questionService.saveQuestion(question);
            if(questionResponse != null) {
                return new ResponseEntity<Question>(questionResponse, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<Question>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="questions/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteQuestion(@PathVariable("id") String questionId) {
        try {
            Boolean questionResponse = questionService.deleteQuestion(questionId);
            if (questionResponse == false) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else {
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
