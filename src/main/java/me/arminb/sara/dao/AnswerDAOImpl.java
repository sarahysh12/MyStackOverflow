package me.arminb.sara.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import me.arminb.sara.entities.Answer;
import me.arminb.sara.entities.Comment;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;


@Repository("answerDAO")
public class AnswerDAOImpl implements AnswerDAO {

    private final static String COLLECTION_NAME = "questions";
    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;


    @Override
    public Answer save(Answer answer) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            if (answer.getId() == null) {
                answer.setId(new ObjectId().toString());
                Document doc = new Document().append("answer_id", new ObjectId(answer.getId())).append("answer", answer.getAnswer())
                        .append("rate", answer.getRate()).append("comments", new ArrayList<Comment>()).append("user_id", answer.getUser())
                        .append("created_date", answer.getCreatedAt()).append("modified_date", answer.getModifiedAt());
                BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(answer.getQuestionId()));
                collection.updateOne(searchQuery, Updates.addToSet("answers", doc));
            } else {
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.append("$set", new BasicDBObject().append("answers.$.answer", answer.getAnswer()).append("answers.$.rate", answer.getRate())
                        .append("answers.$.comments", answer.getComments()).append("answers.$.user_id", answer.getUser())
                );
                BasicDBObject searchQuery = new BasicDBObject().append("answers.answer_id", new ObjectId(answer.getId()));
                Document result = collection.findOneAndUpdate(searchQuery, newDocument);
                if (result == null) {
                    answer = null;
                }
            }
            return answer;
        }
        catch(MongoException e){
                logger.warn("Failed to upsert the answer to the database", e);
                throw new DataAccessException();
            }
        }

    @Override
    public boolean delete(String answerId) throws DataAccessException{
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            BasicDBObject query = new BasicDBObject("answers.answer_id", new ObjectId(answerId));
            BasicDBObject answer = new BasicDBObject("answers", new BasicDBObject( "answer_id",  new ObjectId(answerId)));
            BasicDBObject update = new BasicDBObject("$pull",answer);
            UpdateResult result = collection.updateOne( query, update );
            if(result.getModifiedCount() == 1){
                return true;
            }else{
                return false;
            }
        } catch(MongoException e){
            logger.warn("Failed to delete the answer from the database", e);
            throw new DataAccessException();
        }
    }


}
