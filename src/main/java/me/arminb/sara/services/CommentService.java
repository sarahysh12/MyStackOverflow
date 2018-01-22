package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Comment;
import me.arminb.sara.entities.Question;


public interface CommentService {

    public Question saveComment(String answerId, Comment comment) throws DataAccessException;
    public boolean deleteComment(String commentId) throws DataAccessException;

}
