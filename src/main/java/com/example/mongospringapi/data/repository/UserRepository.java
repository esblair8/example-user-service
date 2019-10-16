package com.example.mongospringapi.data.repository;

import com.example.mongospringapi.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String>, CustomRepository {

    List<User> findAllByName(String name);


}