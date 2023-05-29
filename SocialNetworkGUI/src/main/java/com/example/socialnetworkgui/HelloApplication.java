package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validators.FriendshipValidator;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.repository.db.FriendshipDbRepository;
import com.example.socialnetworkgui.repository.db.UserDbRepository;
import com.example.socialnetworkgui.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}