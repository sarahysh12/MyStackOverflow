package me.arminb.sara.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import me.arminb.sara.entities.Comment;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository("commentDAO")
public class CommentDAOImpl implements CommentDAO {

    private final static String COLLECTION_NAME = "questions";
    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    @Override
    public Comment save(Comment comment) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            comment.setModifiedAtToNow();
            List<BasicDBObject> arrayFilters = new ArrayList<BasicDBObject>();
            if (comment.getId() == null) {
                comment.setId(new ObjectId().toString());
                Document doc = new Document().append("comment_id", new ObjectId(comment.getId())).append("content", comment.getContent())
                        .append("user_id", comment.getUserId()).append("modified_at", comment.getModifiedAt());

                BasicDBObject searchQuery = new BasicDBObject().append("answers.answer_id", new ObjectId(comment.getAnswerId()));
                collection.updateOne(searchQuery, Updates.addToSet("answers.$.comments", doc));
            } else {
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.append("$set", new BasicDBObject().append("answers.$[ans].comments.$[com].content", comment.getContent())
                        .append("answers.$[ans].comments.$[com].modified_at", comment.getModifiedAt()));
                BasicDBObject searchQuery = new BasicDBObject().append("answers.comments.comment_id", new ObjectId(comment.getId()));

                arrayFilters.add(new BasicDBObject("ans.answer_id", new ObjectId(comment.getAnswerId())));
                arrayFilters.add(new BasicDBObject("com.comment_id", new ObjectId(comment.getId())));
                Document result = collection.findOneAndUpdate(searchQuery, newDocument, new FindOneAndUpdateOptions().arrayFilters(arrayFilters));

                if (result == null) {
                    comment = null;
                }
            }
            return comment;
        } catch (MongoException e) {
            logger.warn("Failed to upsert the comment to the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public boolean delete(String commentId) throws DataAccessException{
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            BasicDBObject query = new BasicDBObject("answers.comments.comment_id", new ObjectId(commentId));
            BasicDBObject comments = new BasicDBObject("answers.$.comments", new BasicDBObject( "comment_id",  new ObjectId(commentId)));
            BasicDBObject update = new BasicDBObject("$pull",comments);
            UpdateResult result = collection.updateOne( query, update);
            if(result.getModifiedCount() == 1){
                return true;
            }else{
                return false;
            }
        } catch(MongoException e){
            logger.warn("Failed to delete the comment from the database", e);
            throw new DataAccessException();
        }
    }
}
