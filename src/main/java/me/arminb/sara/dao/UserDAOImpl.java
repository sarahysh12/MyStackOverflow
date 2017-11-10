package me.arminb.sara.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import me.arminb.sara.entities.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    @Autowired
    private MongoDatabase database;
    //private MongoTemplate mongoTemplate;

    @Override
    public void findAll() {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            for (Document doc : collection.find()) {
                System.out.println(doc.toJson());
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void addUser(User user) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            Document doc = new Document("userName", user.getUserName()).append("userEmail", user.getEmail()).append("userPassword",user.getPassword());
            collection.insertOne(doc);
        } catch (Exception e) {
        }
    }

    @Override
    public void findUser(String email) {
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            BasicDBObject query = new BasicDBObject();
            query.append("userEmail", email);
            MongoCursor<Document> cursor = collection.find(query).iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                List list = new ArrayList(doc.values());
                System.out.print(list.get(2));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void delete(String email) {
        MongoCollection<Document> collection = database.getCollection("users");
        BasicDBObject query = new BasicDBObject();
        query.append("userEmail", email); //"mikie@mail.com"
        collection.deleteOne(query);
    }
}
