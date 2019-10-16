package com.example.mongospringapi.grpc.client;

import com.proto.user.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Collections;
import java.util.Map;

import static com.example.mongospringapi.grpc.client.GRpcClientRequestUtils.*;

public class UserGRpcClient {

    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
    private UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(channel);

    public static void main(String[] args) {
        UserGRpcClient client = new UserGRpcClient();
        client.run();
    }

    private void run() {
        //ADD USERS
        String userId1 = addNewUser("Jesse", Collections.singletonMap("set", "world"));
        String userId2 = addNewUser("Walter", Collections.singletonMap("hello", "there"));
        readAllUsers();
        String userId3 = addNewUser("Jesse", Collections.singletonMap("hello", "there world"));
        readUser(userId3);

        //READ BY NAME
        readAllByName("Jesse");

        //UPDATE SETTINGS AND USERNAME
        addOrUpdateUserSetting(userId3, "car", "Toyota Tercel");
        addOrUpdateUserSetting(userId3, "boat", "none");
        addOrUpdateUserSetting(userId3, "car", "none");
        updateUserName(userId3, "Mike");

        //READ SETTINGS
        readUserSettings(userId3);
        readUserSetting(userId3, "hello");

        //DELETE USER SETTINGS
        deleteUserSetting(userId3, "car");
        deleteUserSettings(userId3);
        readUserSettings(userId3);

        //DELETE USER
        deleteUser(userId3);
        readAllUsers();
        deleteUser(userId1);
        deleteUser(userId2);
        readAllUsers();
        channel.shutdown();
    }


    //CREATE
    private String addNewUser(String name, Map<String, String> userSettings) {
        NewUserRequest request = buildNewUserRequest(name, userSettings);
        GRpcUser addedUser = userClient.addNewUser(request).getUser();
        printUserDetails(addedUser, "addNewUser Call");
        return addedUser.getUserId().getId();
    }

    //READ
    private void readAllUsers() {
        userClient.readAllUsers(buildEmptyResponse()).forEachRemaining(response -> printUserDetails(response.getUser(), "getAllUsers call"));
    }

    private void readUser(String id) {
        UserId request = buildUserId(id);
        GRpcUser user = userClient.readUser(request).getUser();
        printUserDetails(user, "getUser call");
    }

    private void readAllByName(String name) {
        UserName request = buildUserName(name);
        userClient.readByName(request).forEachRemaining(response -> printUserDetails(response.getUser(), "getAllUsersByName call"));
    }

    private void readUserSettings(String userId) {
        UserId request = buildUserId(userId);
        Map userMap = userClient.readAllUserSettings(request).getUserSettingsMap();
        System.out.println("all user settings: " + userMap);
    }

    private void readUserSetting(String userId, String key) {
        SingleSettingRequest request = buildSingleSettingRequest(userId, key);
        String value = userClient.readUserSetting(request).getValue();
        System.out.println("user setting: key=" + key + " value=" + value);
    }

    //UPDATE
    private void addOrUpdateUserSetting(String userId, String key, String value) {
        UserId userid = buildUserId(userId);
        AddOrUpdateUserSetting request = AddOrUpdateUserSetting.newBuilder().setUserId(userid).setKey(key).setValue(value).build();
        printUserDetails(userClient.addOrUpdateUserSettings(request).getUser(), "updates user settings");
    }

    private void updateUserName(String userId, String name) {
        UpdateNameRequest request = buildUpdateNameRequest(userId, name);
        GRpcUser user = userClient.updateUserName(request).getUser();
        printUserDetails(user, "updated user name");
    }

    //DELETE
    private void deleteUserSettings(String userId) {
        UserId id = buildUserId(userId);
        GRpcUser user = userClient.deleteUserSettings(id).getUser();
        printUserDetails(user, "deleted user settings");
    }

    private void deleteUserSetting(String userId, String key) {
        SingleSettingRequest request = buildSingleSettingRequest(userId, key);
        GRpcUser user = userClient.deleteUserSetting(request).getUser();
        printUserDetails(user, "deleted a user setting");
    }

    private void deleteUser(String userId) {
        UserId request = buildUserId(userId);
        userClient.deleteUser(request);
        System.out.println("User deleted");
    }
}