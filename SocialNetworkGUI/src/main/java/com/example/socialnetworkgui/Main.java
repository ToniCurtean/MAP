package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.LoginController;
import com.example.socialnetworkgui.domain.validators.FriendshipValidator;
import com.example.socialnetworkgui.domain.validators.MessageValidator;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.repository.db.FriendshipDbRepository;
import com.example.socialnetworkgui.repository.db.MessageDbRepository;
import com.example.socialnetworkgui.repository.db.UserDbRepository;
import com.example.socialnetworkgui.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    UserDbRepository userRepository;
    FriendshipDbRepository friendshipDbRepository;

    MessageDbRepository messageDbRepository;
    Service service;

    @Override
    public void start(Stage primaryStage) throws Exception {
        userRepository=new UserDbRepository("jdbc:postgresql://localhost:5432/socialNetwork","postgres","Mioritmic03",new UserValidator());
        friendshipDbRepository=new FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialNetwork","postgres","Mioritmic03",new FriendshipValidator());
        messageDbRepository=new MessageDbRepository("jdbc:postgresql://localhost:5432/socialNetwork","postgres","Mioritmic03",new MessageValidator());
        service=new Service(userRepository,friendshipDbRepository, messageDbRepository);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 360);
        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(scene);
        primaryStage.show();
        LoginController loginController=fxmlLoader.getController();
        loginController.init();
        loginController.setService(service);
    }

    public static void main(String[] args){
        launch(args);
    }
}
