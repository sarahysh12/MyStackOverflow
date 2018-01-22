package me.arminb.sara.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import me.arminb.sara.entities.Answer;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("answerDAO")
public class AnswerDAOImpl implements AnswerDAO {

    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;


    //@TODO: Check answer exists with findbyId (return not found to user)
    //@TODO: Exception Handling
    @Override
    public Answer saveAnswer(Answer answer) throws DataAccessException {
        MongoCollection<Document> collection = database.getCollection("questions");
        if (answer.getId() == null) {
            answer.setId(new ObjectId().toString());
            Document doc = new Document().append("answer_id", new ObjectId(answer.getId())).append("answer", answer.getAnswer()).append("rate", answer.getRate())
                    .append("comments", answer.getComments()).append("user", answer.getUser());
            BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(answer.getQuestionId()));
            UpdateResult result = collection.updateOne(searchQuery, Updates.addToSet("answers", doc));
        }
        else{
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("answers.$.answer", answer.getAnswer()).append("answers.$.rate", answer.getRate())
                    .append("answers.$.comments", answer.getComments()).append("answers.$.user", answer.getUser())
            );
            BasicDBObject searchQuery = new BasicDBObject().append("answers.answer_id", new ObjectId(answer.getId()));
            collection.findOneAndUpdate(searchQuery, newDocument);
        }
        return answer;
    }

    @Override
    public boolean deleteAnswer(String answerId) throws DataAccessException{
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
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
            logger.warn("Failed to delete the question from the database", e);
            throw new DataAccessException();
        }
    }


}
