package me.arminb.sara.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sun.javadoc.Doc;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Question;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.arminb.sara.Constants.*;

@Repository("questionDAO")
public class QuestionDAOImpl implements QuestionDAO {

    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;


    private Question mapQuestion(Document questionDoc){

        Question question = new Question();

        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Date createDate = null;
        Date modifiedDate = null;
        try {
            if (questionDoc.get("created_date") != null){
                createDate = format.parse(questionDoc.get("created_date").toString());
            }
            if (questionDoc.get("modified_date") != null) {
                modifiedDate = format.parse(questionDoc.get("modified_date").toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> tagList = (List<String>) questionDoc.get("tags");
        question.setTags(tagList);

        question.setId(questionDoc.get("_id").toString());
        question.setCreatedAt(createDate);
        question.setModifiedAt(modifiedDate);
        question.setContent(questionDoc.get("content").toString());
        question.setRate(Integer.parseInt(questionDoc.get("rate").toString()));
        question.setTitle(questionDoc.get("title").toString());
        question.setUser(questionDoc.get("user").toString());

        List<Answer> ansList = (List<Answer>) questionDoc.get("answers");
        question.setAnswers(ansList);

        return question;
    }

    @Override
    public Question findById(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            query.append("_id", new ObjectId(id));
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                Document questionDoc = cursor.next();
                Question question = mapQuestion(questionDoc);
                return question;
            }
            else{
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
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageCount == null){
            pageCount = DEFAULT_PAGE_COUNT;
        }
        List<Question> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            if (title != null) {
                query.append("title", title);
            }
            if (tag != null) {
                query.append("tags", tag);
            }
            MongoCursor<Document> cursor = collection.find(query).skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
            while (cursor.hasNext()) {
                Document questionDoc = cursor.next();
                Question question = mapQuestion(questionDoc);
                list.add(question);
            }
            if (list.size() != 0) {
                return list;
            }
            else {
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to find questions on the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public Question saveQuestion(Question question) throws DataAccessException {
        try{
            MongoCollection<Document> collection = database.getCollection("questions");

            if (question.getId() == null){
                question.setId(new ObjectId().toString());
                question.setCreatedAt(new Date());
                Document doc = new Document("_id", new ObjectId(question.getId())).append("title", question.getTitle()).append("rate", question.getRate())
                        .append("content", question.getContent()).append("answers", new ArrayList<Answer>()).append("user", question.getUser())
                        .append("tags", question.getTags()).append("modified_date", question.getModifiedAt()).append("created_date", question.getCreatedAt());
                collection.insertOne(doc);
            }
            else{
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.append("$set", new BasicDBObject().append("title", question.getTitle()).append("rate", question.getRate())
                        .append("content", question.getContent()).append("answers", new ArrayList<Answer>()).append("user", question.getUser())
                        .append("tags", question.getTags()).append("modified_date", question.getModifiedAt()).append("created_date", question.getCreatedAt())
                );
                BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(question.getId()));
                Document result = collection.findOneAndUpdate(searchQuery, newDocument);
                if (result == null){
                    question = null;
                }
            }
            return question;
        }
        catch(MongoException e){
            logger.warn("Failed to upsert the question to the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public boolean deleteQuestion(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));
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

}
