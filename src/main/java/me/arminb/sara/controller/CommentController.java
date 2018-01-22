package me.arminb.sara.controller;


import me.arminb.sara.controller.models.CommentRequest;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Comment;
import me.arminb.sara.entities.Question;
import me.arminb.sara.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value= "{aid}", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> addComment(@PathVariable("aid") String answerId, @RequestBody CommentRequest commentReq){
        try{
            Comment comment = commentReq.toComment();
            return new ResponseEntity<Question>(commentService.saveComment(answerId, comment), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="{aid}/{cid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Question> updateComment(@PathVariable("aid") String answerId ,
                                                  @PathVariable("cid") String commentId ,
                                                  @RequestBody CommentRequest commentReq){
        try{
            Comment comment = commentReq.toComment();
            comment.setId(commentId);
            return new ResponseEntity<Question>(commentService.saveComment(answerId, comment), HttpStatus.OK);
        }
        catch (DataAccessException e){
            return new ResponseEntity<Question>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
