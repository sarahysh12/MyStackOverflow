package me.arminb.sara.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
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

    Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private MongoDatabase database;

    @Override
    public List<User> findAll() throws DataAccessException {
        List<User> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> iterator = collection.find().iterator();
            while (iterator.hasNext()) {
                String jsonInString = iterator.next().toJson();
                Gson g = new GsonBuilder().create();
                User user = g.fromJson(jsonInString, User.class);
                list.add(user);
            }
            if (list.size() != 0)
                return list;
            else
                return null;
        } catch (MongoException e) {
            logger.warn("Failed to select users due to"+ e);
            throw new DataAccessException();
        }
    }

    @Override
    public User create(User user) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("users").withWriteConcern(WriteConcern.ACKNOWLEDGED);
            user.setId(new ObjectId());
            Document doc = new Document("_id", user.getId()).append("user_name", user.getUsername())
                    .append("user_email", user.getEmail()).append("user_password",user.getPassword());
            collection.insertOne(doc);
            return user;
        } catch (MongoException e) {
            logger.warn("Failed to insert the user due to"+ e);
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
            logger.warn("Failed to select the user due to" + e);
            throw new DataAccessException();
        }
    }

    @Override
    public List<User> find(ObjectId id, String username, String email) throws DataAccessException {
        List<User> list = new ArrayList();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            if (id != null)
                query.append("_id", id);
            if (username != null)
                query.append("user_name", username);
            if (email != null)
                query.append("user_email", email);
            MongoCursor<Document> cursor = collection.find(query).iterator();
            while (cursor.hasNext()) {
                String jsonInString = cursor.next().toJson();
                Gson g = new GsonBuilder().create();
                User user_obj = g.fromJson(jsonInString, User.class);
                list.add(user_obj);
            }
            if (list.size() != 0)
                return list;
            else
                return null;
        } catch (MongoException e) {
            logger.warn("Failed to find the user due to" + e);
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
            if (result.getDeletedCount() == 0)
                return false;
            else
                return true;
        } catch(MongoException e){
            logger.warn("Failed to delete the user due to" + e);
            throw new DataAccessException();
        }
    }

    @Override
    public User update(User user) throws DataAccessException {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("user_email", user.getEmail()).append("user_password", user.getPassword())
                    .append("user_name", user.getUsername()));
            BasicDBObject searchQuery = new BasicDBObject().append("_id", user.getId());
            UpdateResult result = collection.updateOne(searchQuery, newDocument);
            if (result.getModifiedCount() == 0)
                return null;
            else {
                return user;
            }
        } catch (MongoException e) {
            logger.warn("Failed to update the user due to" + e);
            throw new DataAccessException();
        }
    }

}
