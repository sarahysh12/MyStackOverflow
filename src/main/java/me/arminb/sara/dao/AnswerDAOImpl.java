package me.arminb.sara.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
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
import java.util.Date;
import java.util.List;

@Repository("answerDAO")
public class AnswerDAOImpl implements AnswerDAO {

    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;


    public Question findById(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject();
            query.append("_id", new ObjectId(id));
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                Document questionDoc = cursor.next();
                Question question = new Question();

                //Date
                DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
                Date date = null;
                try {
                    date = format.parse(questionDoc.get("date").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                List<String> tagList = (List<String>) questionDoc.get("tags");
                question.setTags(tagList);

                question.setId(questionDoc.get("_id").toString());
                question.setDate(date);
                question.setContent(questionDoc.get("content").toString());
                question.setRate(Integer.parseInt(questionDoc.get("rate").toString()));
                question.setTitle(questionDoc.get("title").toString());
                question.setUser(questionDoc.get("user").toString());

                List<Answer> ansList = (List<Answer>) questionDoc.get("answers");
                question.setAnswers(ansList);

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
    public Question saveAnswer(String questionId, Answer answer) throws DataAccessException {
        if (answer.getId() == null){
            answer.setId(new ObjectId().toString());
        }

        MongoCollection<Document> collection = database.getCollection("questions");
        Document doc = new Document().append("answer_id", new ObjectId(answer.getId())).append("answer", answer.getAnswer()).append("rate", answer.getRate())
                .append("comments", answer.getComments()).append("user", answer.getUser());
        BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(questionId));
        UpdateResult result = collection.updateOne(searchQuery, Updates.addToSet("answers", doc), (new UpdateOptions()).upsert(true) );
        if (result.getModifiedCount() > 0 || result.getUpsertedId() != null){
            return findById(questionId);
        }
        else{
            return null;
        }
    }

    @Override
    public boolean deleteAnswer(String answerId) throws DataAccessException{
        try {

            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject("answers.answer_id", new ObjectId(answerId));
            BasicDBObject fields = new BasicDBObject("answers",
                    new BasicDBObject( "answer_id",  new ObjectId(answerId)));
            BasicDBObject update = new BasicDBObject("$pull",fields);
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
