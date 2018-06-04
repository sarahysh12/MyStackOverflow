package me.arminb.sara.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Comment;
import me.arminb.sara.entities.Question;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.crypto.Cipher;
import javax.print.Doc;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.arminb.sara.Constants.*;

@Repository("questionDAO")
public class QuestionDAOImpl implements QuestionDAO {

    private final static String COLLECTION_NAME = "questions";
    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;


    private List<Comment> parseCommentsDocument(Document answerDoc) {

        List<Document> commentDocList = (List<Document>) answerDoc.get("comments");

        List<Comment> commentList = new ArrayList<Comment>();
        for (int i = 0; i < commentDocList.size(); i++) {
            Comment comment = new Comment();
            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date modifiedDate = null;
            try {
                if (commentDocList.get(i).get("modified_at") != null) {
                    modifiedDate = format.parse(commentDocList.get(i).get("modified_at").toString());
                }
            } catch (ParseException e) {
                logger.warn("Failed to parse the date");
            }

            if(commentDocList.get(i).get("user_id") != null) {
                comment.setUserId(commentDocList.get(i).get("user_id").toString());
            }
            comment.setContent(commentDocList.get(i).get("content").toString());
            comment.setId(commentDocList.get(i).get("comment_id").toString());
            comment.setModifiedAt(modifiedDate);
            comment.setAnswerId(answerDoc.get("answer_id").toString());

            commentList.add(comment);
        }
        return commentList;
    }

    private List<Answer> parseAnswersDocument(Document questionDoc){

        List<Document> answersDocList = (List<Document>) questionDoc.get("answers");

        List<Answer> answersList = new ArrayList<Answer>();
        for (int i = 0; i < answersDocList.size(); i++) {
            Answer answer = new Answer();
            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date modifiedDate = null;
            try {
                if (answersDocList.get(i).get("modified_at") != null) {
                    modifiedDate = format.parse(answersDocList.get(i).get("modified_at").toString());
                }
            } catch (ParseException e) {
                logger.warn("Failed to parse the date");
            }

            answer.setId(answersDocList.get(i).get("answer_id").toString());
            answer.setAnswer(answersDocList.get(i).get("answer").toString());
            answer.setRate(Integer.parseInt(answersDocList.get(i).get("rate").toString()));
            answer.setQuestionId(questionDoc.get("_id").toString());
            if(answersDocList.get(i).get("user_id") != null) {
                answer.setUserId(answersDocList.get(i).get("user_id").toString());
            }
            answer.setModifiedAt(modifiedDate);
            if(answersDocList.get(i).get("comments") != null) {
                answer.setComments(parseCommentsDocument(answersDocList.get(i)));
            }
            answersList.add(answer);
        }

        return answersList;
    }

    private Question parseQuestionDocument(Document questionDoc){

        Question question = new Question();

        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Date modifiedDate = null;
        try {
            if (questionDoc.get("modified_at") != null) {
                modifiedDate = format.parse(questionDoc.get("modified_at").toString());
            }
        } catch (ParseException e) {
            logger.warn("Failed to parse the date");
        }

        List<String> tagList = (List<String>) questionDoc.get("tags");
        question.setTags(tagList);

        question.setId(questionDoc.get("_id").toString());
        question.setModifiedAt(modifiedDate);
        question.setContent(questionDoc.get("content").toString());
        question.setRate(Integer.parseInt(questionDoc.get("rate").toString()));
        question.setTitle(questionDoc.get("title").toString());

        if(questionDoc.get("user_id") != null) {
            question.setUserId(questionDoc.get("user_id").toString());
        }
        if(questionDoc.get("answers") != null) {
            question.setAnswers(parseAnswersDocument(questionDoc));
        }
        return question;
    }

    @Override
    public Question findById(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            BasicDBObject query = new BasicDBObject();
            query.append("_id", new ObjectId(id));
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                Document questionDoc = cursor.next();
                Question question = parseQuestionDocument(questionDoc);
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
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            BasicDBObject query = new BasicDBObject();
            if (title != null) {
                query.append("title", new BasicDBObject("$regex", ".*" + title + ".*"));
            }
            if (tag != null) {
                query.append("tags", new BasicDBObject("$regex", ".*" + tag + ".*"));
            }
            MongoCursor<Document> cursor = collection.find(query).skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
            while (cursor.hasNext()) {
                Document questionDoc = cursor.next();
                Question question = parseQuestionDocument(questionDoc);
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
    public Question save(Question question) throws DataAccessException {
        try{
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            question.setModifiedAtToNow();

            if (question.getId() == null){
                question.setId(new ObjectId().toString());
                Document doc = new Document("_id", new ObjectId(question.getId())).append("title", question.getTitle()).append("rate", question.getRate())
                        .append("content", question.getContent()).append("answers", new ArrayList<Answer>()).append("user_id", question.getUserId())
                        .append("tags", question.getTags()).append("modified_at", question.getModifiedAt());
                collection.insertOne(doc);
            }
            else{
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.append("$set", new BasicDBObject().append("title", question.getTitle()).append("rate", question.getRate())
                        .append("content", question.getContent()).append("tags", question.getTags())
                        .append("modified_at", question.getModifiedAt())
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
    public boolean delete(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
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
