package me.arminb.sara.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static me.arminb.sara.Constants.*;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    private User parseUserDocument(Document userDoc){
        User user = new User();
        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Date modifiedDate = null;
        try {
            if (userDoc.get("modified_date") != null) {
                modifiedDate = format.parse(userDoc.get("modified_date").toString());
            }
        } catch (ParseException e) {
            logger.warn("Failed to parse the date");
        }
        user.setId(userDoc.get("_id").toString());
        user.setUsername(userDoc.get("username").toString());
        user.setPassword(userDoc.get("password").toString());
        user.setEmail(userDoc.get("email").toString());
        user.setModifiedAt(modifiedDate);
        return user;
    }

    @Override
    public User findById(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            query.append("_id", new ObjectId(id));
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                Document userDoc = cursor.next();
                User user = parseUserDocument(userDoc);
                return user;
            }else{
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to fetch user {} from the database", id, e);
            throw new DataAccessException();
        }
    }

    @Override
    public List<User> find(String username, String email, Integer pageNumber, Integer pageCount) throws DataAccessException {
        if (pageNumber == null){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageCount == null){
            pageCount = DEFAULT_PAGE_COUNT;
        }
        List<User> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            if (username != null) {
                query.append("username", new BasicDBObject("$regex", ".*" + username + ".*"));
            }
            if (email != null) {
                query.append("email", new BasicDBObject("$regex", ".*" + email + ".*"));
            }
            MongoCursor<Document> cursor = collection.find(query).skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                User user = parseUserDocument(userDoc);
                list.add(user);
            }
            if (list.size() != 0) {
                return list;
            }
            else {
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to find users on the database", e);
            throw new DataAccessException();
        }
    }


    @Override
    public User save(User user) throws DataAccessException {
        try{
            MongoCollection<Document> collection = database.getCollection("users");

            if (user.getId() == null){
                user.setId(new ObjectId().toString());
                Document doc = new Document("_id", new ObjectId(user.getId())).append("email", user.getEmail()).append("password", user.getPassword())
                        .append("username", user.getUsername()).append("modified_date", user.getModifiedAt());
                collection.insertOne(doc);
            }
            else{
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.append("$set", new BasicDBObject().append("email", user.getEmail()).append("password", user.getPassword())
                        .append("username", user.getUsername()).append("modified_date", user.getModifiedAt())
                );
                BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(user.getId()));
                Document result = collection.findOneAndUpdate(searchQuery, newDocument);
                if (result == null){
                    user = null;
                }
            }
            return user;
        }
        catch(MongoException e){
            logger.warn("Failed to upsert the user to the database", e);
            throw new DataAccessException();
        }
    }


    @Override
    public boolean delete(String id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
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
            logger.warn("Failed to delete the user from the database", e);
            throw new DataAccessException();
        }
    }
}