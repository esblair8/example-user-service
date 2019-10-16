package com.example.mongospringapi.grpc.server;

import com.example.mongospringapi.data.model.User;
import com.proto.user.*;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Map;

public class GRPCServerResponseUtils {

    protected static void sendStreamingResponse(StreamObserver<UserResponse> responseObserver, List<User> users) {
        users.iterator().forEachRemaining(document -> responseObserver.onNext(buildUserResponse(document)));
    }

    protected static UserResponse buildUserResponse(User user) {
        return UserResponse.newBuilder().setUser(documentToUser(user)).build();
    }

    protected static GRpcUser documentToUser(User user) {
        return GRpcUser.newBuilder()
                .setUserId(UserId.newBuilder().setId(user.getUserId()))
                .setCreationDate(user.getCreationDate().toString())
                .setUsername(buildUserName(user.getName()))
                .setUserSettings(buildUserSettings(user.getUserSettings()))
                .build();
    }

    protected static UserName buildUserName(String name) {
        return UserName.newBuilder().setName(name).build();
    }

    protected static UserSettings buildUserSettings(Map<String, String> userSettings) {
        return UserSettings.newBuilder().putAllUserSettings(userSettings).build();
    }
}