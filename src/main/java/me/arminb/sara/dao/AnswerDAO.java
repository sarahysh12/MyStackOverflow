package me.arminb.sara.dao;

import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Question;


public interface AnswerDAO {

    public Question saveAnswer(String questionId, Answer answer) throws DataAccessException;
    public boolean deleteAnswer(String answerId) throws DataAccessException;
}
