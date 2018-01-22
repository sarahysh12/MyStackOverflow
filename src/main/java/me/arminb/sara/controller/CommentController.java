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

@RestController
@RequestMapping
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value= "answers/{aid}", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @RequestMapping(value="answers/{aid}/comments/{cid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Comment> updateComment(@PathVariable("aid") String answerId ,
                                                  @PathVariable("cid") String commentId ,
                                                  @RequestBody CommentRequest commentReq){
        try{
            Comment comment = commentReq.toComment();
            comment.setId(commentId);
            comment.setAnswerId(answerId);
            return new ResponseEntity<Comment>(commentService.saveComment(comment), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Comment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="comments/{cid}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteComment(@PathVariable("cid") String commentId) {
        try {
            return new ResponseEntity<Boolean>(commentService.deleteComment(commentId), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
