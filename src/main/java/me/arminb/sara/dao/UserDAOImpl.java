package me.arminb.sara.dao;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
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

import java.util.ArrayList;
import java.util.List;

import static me.arminb.sara.Constants.*;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    @Override
    public List<User> findAll(Integer pageNumber, Integer pageCount) throws DataAccessException {
        if (pageNumber == null){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageCount == null){
            pageCount = DEFAULT_PAGE_COUNT;
        }
        List<User> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> iterator = collection.find().skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
            while (iterator.hasNext()) {

                Document userDoc = iterator.next();
                User user = new User();
                user.setId(userDoc.get("_id").toString());
                user.setUsername(userDoc.get("username").toString());
                user.setPassword(userDoc.get("username").toString());
                user.setEmail(userDoc.get("email").toString());
                list.add(user);
            }
            if (list.size() != 0) {
                return list;
            }
            else {
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to fetch users from the database", e);
            throw new DataAccessException();
        }
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
                User user = new User();
                user.setId(userDoc.get("_id").toString());
                user.setUsername(userDoc.get("username").toString());
                user.setPassword(userDoc.get("username").toString());
                user.setEmail(userDoc.get("email").toString());
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
                query.append("username", username);
            }
            if (email != null) {
                query.append("email", email);
            }
            if (query.size() != 0) {
                MongoCursor<Document> cursor = collection.find(query).skip(pageCount * (pageNumber - 1)).limit(pageCount).iterator();
                while (cursor.hasNext()) {
                    Document userDoc = cursor.next();
                    User user = new User();
                    user.setId(userDoc.get("_id").toString());
                    user.setUsername(userDoc.get("username").toString());
                    user.setPassword(userDoc.get("username").toString());
                    user.setEmail(userDoc.get("email").toString());
                    list.add(user);
                }
            }
            if (list.size() != 0) {
                return list;
            }
            else {
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to search users on the database", e);
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

    @Override
    public User save(User user) throws DataAccessException {
        try{
            if (user.getId() == null){
                user.setId(new ObjectId().toString());
            }
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("email", user.getEmail()).append("password", user.getPassword()).append("username", user.getUsername()
            ));
            BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(user.getId()));
            UpdateResult result = collection.updateOne(searchQuery, newDocument, (new UpdateOptions()).upsert(true));
            if (result.getModifiedCount() > 0 || result.getUpsertedId() != null){
                return user;
            }
            else{
                return null;
            }
        }
        catch(MongoException e){
            logger.warn("Failed to upsert the user to the database", e);
            throw new DataAccessException();
        }
    }
}