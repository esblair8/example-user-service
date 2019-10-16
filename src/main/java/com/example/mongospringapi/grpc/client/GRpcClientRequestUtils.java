package com.example.mongospringapi.grpc.client;

import com.proto.user.*;

import java.util.Map;

class GRpcClientRequestUtils {

    static Empty buildEmptyResponse() {
        return Empty.newBuilder().build();
    }

    static UserName buildUserName(String name) {
        return UserName.newBuilder()
                .setName(name)
                .build();
    }

    static UserId buildUserId(String userId) {
        return UserId.newBuilder()
                .setId(userId)
                .build();
    }

    static UpdateNameRequest buildUpdateNameRequest(String userId, String name) {
        UserId id = buildUserId(userId);
        UserName userName = buildUserName(name);
        return UpdateNameRequest.newBuilder()
                .setUserId(id)
                .setUserName(userName)
                .build();
    }

    static SingleSettingRequest buildSingleSettingRequest(String userId, String key) {
        return SingleSettingRequest.newBuilder()
                .setUserId(buildUserId(userId))
                .setKey(key)
                .build();
    }

    static NewUserRequest buildNewUserRequest(String name, Map<String, String> userSettings) {
        return NewUserRequest.newBuilder()
                .setUserName(buildUserName(name))
                .setSettings(buildUserSettings(userSettings))
                .build();
    }

    private static UserSettings buildUserSettings(Map<String, String> userSettings) {
        return UserSettings.newBuilder()
                .putAllUserSettings(userSettings)
                .build();
    }

    static void printUserDetails(GRpcUser user, String message) {
        System.out.println(message);
        System.out.println(user.toString());
        System.out.println("DONE");
    }
}