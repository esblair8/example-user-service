package com.example.mongospringapi.service;

import com.example.mongospringapi.data.model.User;
import com.example.mongospringapi.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Map<String, String> getAllSettingsForUser(String id) {
        return userRepository.findById(id).get().getUserSettings();
    }

    public String getUserSetting(String id, String key) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getUserSettings().get(key);
        } else {
            return "user not found";
        }
    }

    public User addOrModifyUserSetting(String id, String key, String value) {
        Optional<User> user = userRepository.findById(id);
        user.get().getUserSettings().put(key, value);
        return userRepository.save(user.get());
    }

    public List<User> findByName(String name) {
        return userRepository.findAllByName(name);
    }

    public Object updateUserWithTwoOperations(String name, String userId) {
        Optional<User> user = getUserById(userId);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setName(name);
            return saveUser(updatedUser);
        } else {
            return "user does not exist";
        }
    }

    public User deleteSettings(String userId) {
        Optional<User> user = getUserById(userId);
        User updateUser = user.get();
        updateUser.setUserSettings(Collections.emptyMap());
        return saveUser(updateUser);
    }


    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        return "user deleted";
    }

    public User deleteUserSetting(String userId, String key) {
        Optional<User> user = getUserById(userId);
        User updateUser = user.get();
        updateUser.removeSetting(key);
        return saveUser(updateUser);

    }

    public User updateUserName(String userId, String name) {
        return userRepository.findAndModifyUserName(userId, name);
    }
}