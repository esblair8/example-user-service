package com.example.mongospringapi.rest;

import com.example.mongospringapi.data.model.User;
import com.example.mongospringapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.getUserById(userId).get();
    }

    @GetMapping(value = "/name/{name}")
    public List<User> findByName(@PathVariable String name) {
        return userService.findByName(name);
    }

    @GetMapping(value = "/settings/{userId}")
    public Object getAllUserSettings(@PathVariable String userId) {
        return userService.getAllSettingsForUser(userId);
    }

    @GetMapping(value = "/settings/{userId}/{key}")
    public String getUserSetting(@PathVariable String userId, @PathVariable String key) {
        return userService.getUserSetting(userId, key);
    }

    @PostMapping(value = "/create")
    public User addNewUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    /**
     * REPLACES ENTIRE OBJECT
     * INCLUDING DATE
     */
    @PutMapping(value = "/update")
    public Object updateUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    /**
     * Add or Modify User Settings
     */
    @PatchMapping(value = "/settings/{userId}/{key}/{value}")
    public User addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
        return userService.addOrModifyUserSetting(userId, key, value);
    }

    /**
     * REPLACES NAME - ID IN PATH VARIABLE FORMAT
     * PRESERVES DATE
     */
    @PatchMapping(value = "/{userId}")
    public Object patchUser(@RequestBody User user, @PathVariable String userId) {
        return userService.updateUserWithTwoOperations(user.getName(), userId);
    }

    /**
     * REPLACES NAME - ID IN BODY FORMAT
     * PRESERVES DATE
     */
    @PatchMapping(value = "/update")
    public Object patchUserBody(@RequestBody User user) {
        return userService.updateUserWithTwoOperations(user.getName(), user.getUserId());
    }

    @PatchMapping(value = "/updateUserName")
    public Object updateUserName(@RequestBody User user) {
        return userService.updateUserName(user.getUserId(), user.getName());
    }

    @DeleteMapping(value = "/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return userService.deleteUser(userId);
    }

    @DeleteMapping(value = "/settings/{userId}")
    public Object deleteUserSettings(@PathVariable String userId) {
        return userService.deleteSettings(userId);
    }

    @DeleteMapping(value = "/settings/{userId}/{key}")
    public User deleteAUserSetting(@PathVariable String userId, @PathVariable String key) {
        return userService.deleteUserSetting(userId, key);
    }
}