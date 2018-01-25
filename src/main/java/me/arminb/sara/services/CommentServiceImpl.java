package me.arminb.sara.services;

import me.arminb.sara.dao.CommentDAO;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentDAO commentDAO;

    @Override
    public Comment saveComment(Comment comment) throws DataAccessException {
        try{
            return commentDAO.save(comment);
        }
        catch (DataAccessException e){
            throw e;
        }
    }

    @Override
    public boolean deleteComment(String commentId) throws DataAccessException {
        try{
            return commentDAO.delete(commentId);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

}
