package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Answer;

public interface AnswerService {

    public Answer saveAnswer(Answer answer) throws DataAccessException;
    public boolean deleteAnswer(String answerId) throws DataAccessException;

}
