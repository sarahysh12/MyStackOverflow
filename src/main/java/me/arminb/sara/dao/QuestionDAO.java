package me.arminb.sara.dao;

import me.arminb.sara.entities.Question;
import org.bson.types.ObjectId;

import java.util.List;

public interface QuestionDAO{
        public List<Question> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException;
        public Question findById(ObjectId id) throws DataAccessException;
        public List<Question> find(String title, String tag, Integer pageNumber, Integer pageCount) throws DataAccessException;
        public boolean delete(ObjectId id) throws DataAccessException;
        public Question save(Question question) throws DataAccessException;

}