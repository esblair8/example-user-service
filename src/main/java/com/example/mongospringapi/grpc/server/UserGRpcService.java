package com.example.mongospringapi.grpc.server;

import com.example.mongospringapi.data.model.User;
import com.example.mongospringapi.service.UserService;
import com.proto.user.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

import static com.example.mongospringapi.grpc.server.GRPCServerResponseUtils.*;

@GRpcService
public class UserGRpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    @Autowired
    public UserGRpcService(UserService userService) {
        this.userService = userService;
    }

    //CREATE
    @Override
    public void addNewUser(NewUserRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = new User();
        user.setName(request.getUserName().getName());
        user.setUserSettings(request.getSettings().getUserSettingsMap());
        User savedUser = userService.saveUser(user);
        responseObserver.onNext(buildUserResponse(savedUser));
        responseObserver.onCompleted();
    }

    //READ
    @Override
    public void readUser(UserId request, StreamObserver<UserResponse> responseObserver) {
        Optional<User> user = userService.getUserById(request.getId());

        if (user.isPresent()) {
            UserResponse response = buildUserResponse(user.get());
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("User not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void readAllUsers(Empty request, StreamObserver<UserResponse> responseObserver) {
        sendStreamingResponse(responseObserver, userService.getAllUsers());
        responseObserver.onCompleted();
    }

    @Override
    public void readByName(UserName request, StreamObserver<UserResponse> responseObserver) {
        sendStreamingResponse(responseObserver, userService.findByName(request.getName()));
        responseObserver.onCompleted();
    }

    @Override
    public void readAllUserSettings(UserId request, StreamObserver<UserSettings> responseObserver) {
        Map<String, String> settings = userService.getAllSettingsForUser(request.getId());
        UserSettings response = buildUserSettings(settings);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void readUserSetting(SingleSettingRequest request, StreamObserver<SingleSettingResponse> responseObserver) {
        String setting = userService.getUserSetting(request.getUserId().getId(), request.getKey());
        SingleSettingResponse response = SingleSettingResponse.newBuilder().setValue(setting).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //UPDATE
    @Override
    public void updateUserName(UpdateNameRequest request, StreamObserver<UserResponse> responseObserver) {
        User updatedUser = userService.updateUserName(request.getUserId().getId(), request.getUserName().getName());
        responseObserver.onNext(buildUserResponse(updatedUser));
        responseObserver.onCompleted();
    }

    @Override
    public void addOrUpdateUserSettings(AddOrUpdateUserSetting request, StreamObserver<UserResponse> responseObserver) {
        User updatedUser = userService.addOrModifyUserSetting(request.getUserId().getId(), request.getKey(), request.getValue());
        responseObserver.onNext(buildUserResponse(updatedUser));
        responseObserver.onCompleted();
    }

    //DELETE
    @Override
    public void deleteUser(UserId request, StreamObserver<Empty> responseObserver) {
        userService.deleteUser(request.getId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserSettings(UserId request, StreamObserver<UserResponse> responseObserver) {
        User updatedUser = userService.deleteSettings(request.getId());
        responseObserver.onNext(buildUserResponse(updatedUser));
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserSetting(SingleSettingRequest request, StreamObserver<UserResponse> responseObserver) {
        User updatedUser = userService.deleteUserSetting(request.getUserId().getId(), request.getKey());
        responseObserver.onNext(buildUserResponse(updatedUser));
        responseObserver.onCompleted();
    }


}