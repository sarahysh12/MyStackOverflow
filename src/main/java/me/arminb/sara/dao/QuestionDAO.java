package me.arminb.sara.dao;

import me.arminb.sara.entities.Question;

import java.util.List;

public interface QuestionDAO{

        public Question findById(String id) throws DataAccessException;
        public List<Question> find(String title, String tag, Integer pageNumber, Integer pageCount) throws DataAccessException;
        public Question saveQuestion(Question question) throws DataAccessException;
        public boolean deleteQuestion(String questionId) throws DataAccessException;

}