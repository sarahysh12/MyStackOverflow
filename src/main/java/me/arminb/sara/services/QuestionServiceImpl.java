package me.arminb.sara.services;

import me.arminb.sara.dao.DataAccessException;
import me.arminb.sara.dao.QuestionDAO;
import me.arminb.sara.entities.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("questionService")
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public Question findQuestionById(String id) throws DataAccessException {
        try{
            return questionDAO.findById(id);
        }catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public List<Question> findQuestion(String title, String tag, Integer pageNumber, Integer pageCount) throws DataAccessException {
        try{
            return questionDAO.find(title, tag, pageNumber, pageCount);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public Question saveQuestion(Question question) throws DataAccessException {
        try{
            return questionDAO.save(question);
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    @Override
    public boolean deleteQuestion(String id) throws DataAccessException {
        try{
            return questionDAO.delete(id);
        }
        catch(DataAccessException e){
            throw e;
        }
    }



}
