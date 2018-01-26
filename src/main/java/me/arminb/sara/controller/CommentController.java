package me.arminb.sara.controller;


import me.arminb.sara.controller.models.CommentRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Comment;
import me.arminb.sara.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value= "/answers/{aid}/comments", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Comment> addComment(@PathVariable("aid") String answerId, @RequestBody CommentRequest commentReq){
        try{
            Comment comment = commentReq.toComment();
            comment.setAnswerId(answerId);
            return new ResponseEntity<Comment>(commentService.saveComment(comment), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Comment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/answers/{aid}/comments/{cid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Comment> updateComment(@PathVariable("cid") String commentId ,
                                                 @PathVariable("aid") String answerId ,
                                                  @RequestBody CommentRequest commentReq){
        try{
            Comment comment = commentReq.toComment();
            comment.setId(commentId);
            comment.setAnswerId(answerId);
            Comment commentResponse = commentService.saveComment(comment);
            if (commentResponse == null){
                return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
            }
            else {
                 return new ResponseEntity<Comment>(HttpStatus.OK);
             }
        }
        catch (DataAccessException e){
            return new ResponseEntity<Comment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/comments/{cid}", method = RequestMethod.DELETE)
    public ResponseEntity deleteComment(@PathVariable("cid") String commentId) {
        try {
            Boolean commentResponse = commentService.deleteComment(commentId);
            if(commentResponse == false){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        catch (DataAccessException e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
