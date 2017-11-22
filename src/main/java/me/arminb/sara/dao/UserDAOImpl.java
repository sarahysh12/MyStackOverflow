package me.arminb.sara.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                Document doc = iterator.next();
                User user = new User();
                user.setId((ObjectId)doc.get("_id"));
                user.setUsername(doc.get("user_name").toString());
                user.setEmail(doc.get("user_email").toString());
                user.setPassword(doc.get("user_password").toString());
                list.add(user);
            }
        } catch (MongoException e) {
            logger.info("Failed to select users due to"+ e);
        } finally {
            return list;
        }
    }

    @Override
    public User create(User user) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            user.setId(new ObjectId());
            Document doc = new Document("_id", user.getId()).append("user_name", user.getUsername())
                    .append("user_email", user.getEmail()).append("user_password",user.getPassword());
            collection.insertOne(doc);
            return find(user.getId().toString());
        } catch (MongoException e) {
            logger.info("Failed to insert the user due to"+ e);
            return null;
        }
    }

    @Override
    public User find(String id) {
        try {
            User user = new User();
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            query.append("_id", new ObjectId(id));
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                Document doc = cursor.next();
                user.setId((ObjectId)doc.get("_id"));
                user.setUsername(doc.get("user_name").toString());
                user.setEmail(doc.get("user_email").toString());
                user.setPassword(doc.get("user_password").toString());
                return user;
            }else{
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to select the user due to" + e);
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));
            DeleteResult result = collection.deleteOne(query);
            if (result.getDeletedCount() == 0)
                return false;
            else
                return true;
        } catch(MongoException e){
            logger.warn("Failed to delete the user du to" + e);
            return false;
        }
    }

    @Override
    public User update(String id, User user) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("user_email", user.getEmail()).append("user_password", user.getPassword())
                    .append("user_name", user.getUsername()));
            BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(id));
            UpdateResult result = collection.updateOne(searchQuery, newDocument);
            if (result.getModifiedCount() == 0)
                return null;
            else
                return find(id);
        } catch (MongoException e) {
            logger.warn("Failed to update the user due to" + e);
            return null;
        }
    }

}
