package me.arminb.sara.controller;

import me.arminb.sara.controller.models.AnswerRequest;
import me.arminb.sara.controller.models.CommentRequest;
import me.arminb.sara.controller.models.QuestionRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Comment;
import me.arminb.sara.entities.Question;
import me.arminb.sara.services.QuestionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.tools.java.SyntaxError;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Question>> findAll(@RequestParam(value="page", required=false) Integer pageNumber,
                                              @RequestParam(value="pageCount", required = false) Integer pageCount) {
        try {
            List<Question> questions = questionService.findAll(pageNumber, pageCount);

            return new ResponseEntity<List<Question>>(questions, HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<List<Question>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<Question> findById(@PathVariable("id") String id) {
        try {
            Question question = questionService.findById(id);
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

    @RequestMapping(value="/search",method = RequestMethod.GET)
    public  ResponseEntity<List<Question>> find( @RequestParam(value="title", required=false) String title,
                                             @RequestParam(value="tag", required = false) String tag,
                                             @RequestParam(value="page", required = false) Integer pageNumber,
                                             @RequestParam(value="pageCount", required = false) Integer pageCount) {
        try {
            List<Question> questions = questionService.find(title, tag, pageNumber, pageCount);
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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionRequest questionReq) {
        try {
            Question question = questionReq.toQuestion();
            return new ResponseEntity<Question>(questionService.saveQuestion(question), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.PUT, consumes =  MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> updateQuestion(@PathVariable("id") String id, @RequestBody QuestionRequest questionReq) {
        try {
            Question question = questionReq.toQuestion();
            question.setId(id);
            Question question_obj = questionService.saveQuestion(question);
            if(question_obj != null) {
                return new ResponseEntity<Question>(question_obj, HttpStatus.OK);
            } else {
                return new ResponseEntity<Question>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteQuestion(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<Boolean>(questionService.deleteQuestion(id), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
