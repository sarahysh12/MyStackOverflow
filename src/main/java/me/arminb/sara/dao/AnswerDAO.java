package me.arminb.sara.dao;

import me.arminb.sara.entities.Answer;


public interface AnswerDAO {

    public Answer saveAnswer(Answer answer) throws DataAccessException;
    public boolean deleteAnswer(String answerId) throws DataAccessException;
}
