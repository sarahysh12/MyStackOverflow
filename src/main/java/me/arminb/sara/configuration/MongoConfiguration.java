package me.arminb.sara.configuration;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {

    @Value("${mongodb.port}")
    int portNumber;

    @Value("${mongodb.url}")
    String url;

    @Bean
    public MongoDatabase mongoDatabase(){
        MongoClient client = new MongoClient(new ServerAddress(url, portNumber));
        MongoDatabase db = client.getDatabase("stackoverflow");
        return db;
    }
}
