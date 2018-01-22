package me.arminb.sara.dao;

import me.arminb.sara.entities.Comment;


public interface CommentDAO {

    public Comment saveComment(Comment comment) throws DataAccessException;
    public boolean deleteComment(String commentId) throws DataAccessException;

}
