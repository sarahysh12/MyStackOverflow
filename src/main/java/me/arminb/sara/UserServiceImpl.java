package me.arminb.sara;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoDatabase database;


    public void addUser(UserBean user) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document doc = new Document("userName", user.getUserName()).append("userEmail", user.getEmail()).append("userPassword",user.getPassword());
        collection.insertOne(doc);
    }

    public UserBean getUser(long userid) {
        return null;
    }
}