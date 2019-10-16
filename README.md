# Example User Service 

Simple CRUD app that Integrates Spring Boot, MongoDB and gRPC

The services exposes a simple 'User' collection, stored in MongoDB

The Services also exposes REST endpoints for the CRUD operations for a comparison of style as well as a gRPC API

gRPC generates client code from a proto file, which can be called to marshal requests and responses to the
service

Rest uses JSON and/or Request Params to describe resources which can be operated on

* Rest API Endpoints are exposed on port 8082
* gRPC API is exposed on port 6565

### API definition

| Operation                                                   | REST Endpoint                                        |gRPC Client Function                        |
|-------------------------------------------------------------|------------------------------------------------------|--------------------------------------------|
| create user                                                 | POST /user/create (requires JSON in body)            | addNewUser(newUserRequest)                 | 
| list all users                                              | GET /user                                            | readAllUsers(emptyRequest)                 | 
| list user                                                   | GET /user/{userId}                                   | readUser(userId)                           | 
| list users by name                                          | GET /user/name/{name}                                | readByName(UserName)                       |   
| read a users setting                                        | GET /user/settings/{userId}/{key}                    | readUserSetting(SingleSettingRequest)      |   
| update user                                                 | PUT /user/update                                     | updateUserName(updateNameRequest)          |   
| update user name (2 database operations - find then insert) | PATCH /user/update                                   | N/A                                        |   
| update user name (1 database operation - find and modify)   | PATCH /user/updateUserName (new name and id in body) | updateUserName(userId, name)               |   
| update user name                                            | PATCH /user/{userId} (just new name in body)         | updateUserName(userId, name)               |  
| add or modify user setting                                  | PATCH /user/settings/{userId}/{key}/{value}          | addOrUpdateUserSetting(userId, key, value) |   
| delete all user settings                                    | DELETE /user/settings/{userId}                       | deleteUserSettings(userId)                 |   
| delete a user setting                                       | DELETE user/settings/{userId}/{key}                  | deleteUserSetting(userId, key)             |   
| delete user                                                 | DELETE /user/{userId}                                | deleteUser(userId)                         |   


#### Requirements

Java 8, Gradle, MongoDB, Docker
 
#### Mongo

Run MongoDB easily in Docker

```$ docker run --name some-mongo -d -p 27017:27017 mongo:latest```

Use MongoDB Compass GUI to easily view collections and data
 
#### GenerateRPC Code
 
Generate to gRPC code form the user.proto file

```$ ./gradlew clean generateProto```

#### Run the Spring Boot Server Application

Start the application server

```$ ./gradlew bootRun``` 

#### Run the client

Output will be printed to console

```$ .gradlew run```

#### Running the tests

Tests to be added

```$ ./gradlew test```