package com.example.mongospringapi.data.repository;

import com.example.mongospringapi.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserRepositoryImpl implements CustomRepository {

    private final MongoOperations operations;

    @Autowired
    public UserRepositoryImpl(MongoTemplate operations) {
        this.operations = operations;
    }

    @Override
    public User findAndModifyUserName(String userId, String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.set("name", name);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        return operations.findAndModify(query, update, options, User.class);
    }


}
