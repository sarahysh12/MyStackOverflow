package me.arminb.sara.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    public static final int PAGE_SIZE = 3;
    Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    @Override
    public List<User> findAll(int pageNumber) throws DataAccessException {
        List<User> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> iterator = collection.find().skip(PAGE_SIZE * (pageNumber - 1)).limit(PAGE_SIZE).iterator();
            while (iterator.hasNext()) {
                String jsonInString = iterator.next().toJson();
                Gson g = new GsonBuilder().create();
                User user = g.fromJson(jsonInString, User.class);
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
    public User findById(ObjectId id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            query.append("_id", id);
            MongoCursor<Document> cursor = collection.find(query).iterator();
            if (cursor.hasNext()) {
                String jsonInString = cursor.next().toJson();
                Gson g = new GsonBuilder().create();
                User user = g.fromJson(jsonInString, User.class);
                return user;
            }else{
                return null;
            }
        } catch (MongoException e) {
            logger.warn("Failed to fetch this user from the database", e);
            throw new DataAccessException();
        }
    }

    @Override
    public List<User> find(String username, String email, int pageNumber) throws DataAccessException {
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
                MongoCursor<Document> cursor = collection.find(query).skip(PAGE_SIZE * (pageNumber - 1)).limit(PAGE_SIZE).iterator();
                while (cursor.hasNext()) {
                    String jsonInString = cursor.next().toJson();
                    Gson g = new GsonBuilder().create();
                    User user_obj = g.fromJson(jsonInString, User.class);
                    list.add(user_obj);
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
    public boolean delete(ObjectId id) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            query.put("_id", id);
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
                user.setId(new ObjectId());
            }
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("email", user.getEmail()).append("password", user.getPassword()).append("username", user.getUsername()
            ));
            BasicDBObject searchQuery = new BasicDBObject().append("_id", user.getId());
            UpdateResult result = collection.updateOne(searchQuery, newDocument, (new UpdateOptions()).upsert(true));
            if (result.getModifiedCount() > 0 | result.getUpsertedId() != null){
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