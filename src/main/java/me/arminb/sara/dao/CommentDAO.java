package me.arminb.sara.dao;

import me.arminb.sara.entities.Comment;
import me.arminb.sara.entities.Question;


public interface CommentDAO {

    public Question saveComment(String answerId, Comment comment) throws DataAccessException;
    public boolean deleteComment(String commentId) throws DataAccessException;

}
