package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.entities.Question;

import java.util.List;

public interface  QuestionService {
    public List<Question> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException;
    public Question findById(String id) throws DataAccessException;
    public List<Question> find(String title, String tag, Integer pageNumber, Integer pageCount) throws DataAccessException;
    public boolean delete(String id) throws DataAccessException;
    public Question save(Question question) throws DataAccessException;
}

