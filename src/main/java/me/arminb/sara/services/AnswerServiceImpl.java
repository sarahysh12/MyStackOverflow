package me.arminb.sara.services;

import me.arminb.sara.dao.AnswerDAO;
import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("answerService")
public class AnswerServiceImpl implements AnswerService{

    @Autowired
    private AnswerDAO answerDAO;

    @Override
    public Question saveAnswer(String questionId, Answer answer) throws DataAccessException {
        try{
            return answerDAO.saveAnswer(questionId, answer);
        }
        catch (DataAccessException e){
            throw e;
        }
    }

    @Override
    public boolean deleteAnswer(String answerId) throws DataAccessException {
        try{
            return answerDAO.deleteAnswer(answerId);
        }
        catch(DataAccessException e){
            throw e;
        }
    }
}
