package me.arminb.sara.dao;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import me.arminb.sara.entities.Question;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static me.arminb.sara.constants.PAGE_COUNT;
import static me.arminb.sara.constants.PAGE_NUMBER;

@Repository("questionDAO")
public class QuestionDAOImpl implements QuestionDAO {

    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    @Override
    public List<Question> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException {
        if (pageNumber == null){
            pageNumber = PAGE_NUMBER;
        }
        if (pageCount == null){
            pageCount = PAGE_COUNT;
        }
        List<Question> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            MongoCursor<Document> iterator = collection.find().skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
            while (iterator.hasNext()) {
                String jsonInString = iterator.next().toJson();
                Gson g = new GsonBuilder().create();
                Question question = g.fromJson(jsonInString, Question.class);
                list.add(question);
            }
            if (list.size() != 0) {
                return list;
            }
            else {
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to fetch questions from the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public Question findById(ObjectId id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            query.append("_id", id);
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                String jsonInString = cursor.next().toJson();
                Gson g = new GsonBuilder().create();
                Question question = g.fromJson(jsonInString, Question.class);
                return question;
            }else{
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to fetch question {} from the database", id, e);
            throw new DataAccessException();
        }
    }

    @Override
    public List<Question> find(String title, String tag, Integer pageNumber, Integer pageCount) throws DataAccessException {
        if (pageNumber == null){
            pageNumber = PAGE_NUMBER;
        }
        if (pageCount == null){
            pageCount = PAGE_COUNT;
        }
        List<Question> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            if (title != null) {
                query.append("title", title);
            }
            if (tag != null) {
                query.append("tag", tag);
            }
            if (query.size() != 0) {
                MongoCursor<Document> cursor = collection.find(query).skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
                while (cursor.hasNext()) {
                    String jsonInString = cursor.next().toJson();
                    Gson g = new GsonBuilder().create();
                    Question question_obj = g.fromJson(jsonInString, Question.class);
                    list.add(question_obj);
                }
            }
            if (list.size() != 0) {
                return list;
            }
            else {
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to search questions on the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public boolean delete(ObjectId id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            query.put("_id", id);
            DeleteResult result = collection.deleteOne(query);
            if (result.getDeletedCount() == 0) {
                return false;
            }
            else {
                return true;
            }
        } catch(MongoException e){
            logger.warn("Failed to delete the question from the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public Question save(Question question) throws DataAccessException {
        try{
            if (question.getId() == null){
                question.setId(new ObjectId());
            }
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("title", question.getTitle()).append("rate", question.getRate())
                    .append("content", question.getContent()).append("answers", question.getAnswers()).append("user", question.getUser())
                    .append("date", question.getDate()).append("tags", question.getTags())
            );
            BasicDBObject searchQuery = new BasicDBObject().append("_id", question.getId());
            UpdateResult result = collection.updateOne(searchQuery, newDocument, (new UpdateOptions()).upsert(true));
            if (result.getModifiedCount() > 0 || result.getUpsertedId() != null){
                return question;
            }
            else{
                return null;
            }
        }
        catch(MongoException e){
            logger.warn("Failed to upsert the question to the database", e);
            throw new DataAccessException();
        }
    }

}
