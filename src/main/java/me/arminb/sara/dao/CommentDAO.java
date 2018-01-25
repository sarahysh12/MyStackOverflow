package me.arminb.sara.dao;

import me.arminb.sara.entities.Comment;


public interface CommentDAO {

    public Comment save(Comment comment) throws DataAccessException;
    public boolean delete(String commentId) throws DataAccessException;

}
