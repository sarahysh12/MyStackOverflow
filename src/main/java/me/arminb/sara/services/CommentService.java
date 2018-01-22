package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Comment;


public interface CommentService {

    public Comment saveComment(Comment comment) throws DataAccessException;
    public boolean deleteComment(String commentId) throws DataAccessException;

}
