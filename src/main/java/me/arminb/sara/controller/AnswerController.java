package me.arminb.sara.controller;

import me.arminb.sara.controller.models.AnswerRequest;
import me.arminb.sara.controller.models.CommentRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Comment;
import me.arminb.sara.entities.Question;
import me.arminb.sara.services.AnswerService;
import me.arminb.sara.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(value= "/{qid}", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> addAnswer(@PathVariable("qid") String questionId, @RequestBody AnswerRequest answerReq){
        try{
            Answer answer = answerReq.toAnswer();
            return new ResponseEntity<Question>(answerService.saveAnswer(questionId, answer), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/{qid}/{aid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> updateAnswer(@PathVariable("qid") String questionId,
                                                 @PathVariable("aid") String answerId ,
                                                 @RequestBody AnswerRequest answerReq){
        try{
            Answer answer = answerReq.toAnswer();
            answer.setId(answerId);
            return new ResponseEntity<Question>(answerService.saveAnswer(questionId, answer), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value="/{aid}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteAnswer(@PathVariable("aid") String answerId) {
        try {
            return new ResponseEntity<Boolean>(answerService.deleteAnswer(answerId), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
