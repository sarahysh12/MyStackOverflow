package me.arminb.sara.dao;

import me.arminb.sara.entities.Answer;


public interface AnswerDAO {

    public Answer save(Answer answer) throws DataAccessException;
    public boolean delete(String answerId) throws DataAccessException;
}
