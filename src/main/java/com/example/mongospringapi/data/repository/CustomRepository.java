package com.example.mongospringapi.data.repository;

import com.example.mongospringapi.data.model.User;

public interface CustomRepository {

    User findAndModifyUserName(String userId, String name);
}
