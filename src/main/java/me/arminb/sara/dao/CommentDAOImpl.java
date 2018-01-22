package me.arminb.sara.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import me.arminb.sara.entities.Comment;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("commentDAO")
public class CommentDAOImpl implements CommentDAO {

    Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    //@TODO: Check answer exists with findbyId (return not found to user)
    //@TODO: Exception Handling
    @Override
    public Comment saveComment(Comment comment) throws DataAccessException {
        MongoCollection<Document> collection = database.getCollection("questions");
        if (comment.getId() == null) {
            comment.setId(new ObjectId().toString());
            Document doc = new Document().append("comment_id", new ObjectId(comment.getId())).append("content", comment.getContent())
                    .append("user", comment.getUser());
            BasicDBObject searchQuery = new BasicDBObject().append("answer_id", new ObjectId(comment.getAnswerId()));
            collection.updateOne(searchQuery, Updates.addToSet("comments", doc));
        }
        return comment;
    }

    @Override
    public boolean deleteComment(String commentId) throws DataAccessException{
        try {
            MongoCollection<Document> collection = database.getCollection("questions");
            BasicDBObject query = new BasicDBObject("comment_id", new ObjectId(commentId));
            BasicDBObject comments = new BasicDBObject("answers.comments", new BasicDBObject( "comment_id",  new ObjectId(commentId)));
            BasicDBObject update = new BasicDBObject("$pull",comments);
            UpdateResult result = collection.updateOne( query, update);
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
