package me.arminb.sara.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

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
            return list;
        } catch (Exception e) {
            return list; //??
        }
    }

    @Override
    public void create(User user) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            user.setId(new ObjectId());
            Document doc = new Document("_id", user.getId()).append("user_name", user.getUsername())
                    .append("user_email", user.getEmail()).append("user_password",user.getPassword());
            collection.insertOne(doc);
        } catch (Exception e) {
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
            while (cursor.hasNext()) { //if or while
                Document doc = cursor.next();
                user.setId((ObjectId)doc.get("_id"));
                user.setUsername(doc.get("user_name").toString());
                user.setEmail(doc.get("user_email").toString());
                user.setPassword(doc.get("user_password").toString());
            }
            return user;
        } catch (Exception e) {
            return new User(); //???
        }
    }

    @Override
    public void delete(String id) {
        MongoCollection<Document> collection = database.getCollection("users");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        collection.deleteOne(query);

    }

    @Override
    public void update(String id, User user) {
        MongoCollection<Document> collection = database.getCollection("users");
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("user_email", user.getEmail()).append("user_password", user.getPassword())
                .append("user_name", user.getUsername()));
        BasicDBObject searchQuery = new BasicDBObject().append("_id", new ObjectId(id));
        collection.updateOne(searchQuery, newDocument);
    }

}
