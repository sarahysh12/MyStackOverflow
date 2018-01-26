package me.arminb.sara.controller;

import me.arminb.sara.controller.models.AnswerRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    //TODO: Add find method

    @RequestMapping(value= "/questions/{qid}/answers", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Answer> addAnswer(@PathVariable("qid") String questionId, @RequestBody AnswerRequest answerReq){
        try{
            Answer answer = answerReq.toAnswer();
            answer.setQuestionId(questionId);
            return new ResponseEntity<Answer>(answerService.saveAnswer(answer), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Answer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/answers/{aid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Answer> updateAnswer(@PathVariable("aid") String answerId ,
                                               @RequestBody AnswerRequest answerReq){
        try{
            Answer answer = answerReq.toAnswer();
            answer.setId(answerId);
            Answer answerResponse = answerService.saveAnswer(answer);
            if (answerResponse != null){
                return new ResponseEntity<Answer>(answerResponse, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<Answer>(HttpStatus.NOT_FOUND);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity<Answer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value="/answers/{aid}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAnswer(@PathVariable("aid") String answerId) {
        try {
            Boolean answerResponse = answerService.deleteAnswer(answerId);
            if (answerResponse == false){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
