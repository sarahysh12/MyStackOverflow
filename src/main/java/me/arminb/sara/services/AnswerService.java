package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Question;

public interface AnswerService {

    public Question saveAnswer(String answerId, Answer answer) throws DataAccessException;
    public boolean deleteAnswer(String answerId) throws DataAccessException;

}
