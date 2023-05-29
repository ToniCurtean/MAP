package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.Main;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repository.exceptions.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {
    private Service service;

    @FXML
    private TextField emailText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    public void setService(Service service){
        this.service=service;
    }


    public void init(){
        emailText.setOnKeyPressed(event -> {
            if(event.getCode()==KeyCode.ENTER){
                passwordText.requestFocus();
            }
        });

        passwordText.setOnKeyPressed(event -> {
            if(event.getCode()== KeyCode.ENTER){
                try{
                    onLoginButtonClick(new ActionEvent());
                }catch(IOException e1){
                    throw new RuntimeException(e1);
                }catch(RepositoryException e2){

                }
            }
        });
    }


    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, RepositoryException{
        if(service.checkLogin(emailText.getText(),passwordText.getText())){
            User loggedUser=service.getUserByEmail(emailText.getText());
            FXMLLoader loader=new FXMLLoader(Main.class.getResource("main-view.fxml"));
            Scene scene=new Scene(loader.load(),800,390);
            Stage stage=new Stage();
            stage.setTitle("Welcome!");
            ///stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
            MainController mainPageController=loader.getController();
            mainPageController.setService(service);
            mainPageController.setLoggedUser(loggedUser);
            mainPageController.init();
            ((Stage) loginButton.getScene().getWindow()).close();
        } else {
            UIAlert.showMessage(null, Alert.AlertType.ERROR, "Can't log in!", "Email or password incorrect!");
            passwordText.setText("");
        }

    }

    public void onSignupButtonClick(ActionEvent actionEvent)throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 360);
        Stage stage = new Stage();
        stage.setTitle("Sing Up");
        ///stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
        SignupController signUpController = fxmlLoader.getController();
        signUpController.setService(service);
        signUpController.init();
    }


}
