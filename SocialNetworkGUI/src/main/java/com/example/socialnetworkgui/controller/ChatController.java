package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repository.exceptions.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.SyncFailedException;

public class ChatController {
    private Service service;
    private User loggedUser;
    private User otherUser;

    @FXML
    private Label otherUserName;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField messageField;

    @FXML
    private ListView<String> messagesView;

    private final ObservableList<String> messages = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }

    public void init(){
        otherUserName.setText("You are chating to "+otherUser.getFirstName()+" "+otherUser.getLastName());
        messagesView.setItems(messages);
        messages.clear();
        messages.addAll(service.getMessages(loggedUser.getId(),otherUser.getId()));
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                onSendMessageButtonClick(new ActionEvent());
        });
    }

    public void onSendMessageButtonClick(ActionEvent e){
        if(messageField.getText().strip().equals(""))
            return;
        String text= messageField.getText();
        service.addMessage(loggedUser.getId(),otherUser.getId(),text);
        messagesView.setItems(messages);
        messages.clear();
        messages.addAll(service.getMessages(loggedUser.getId(),otherUser.getId()));
        messageField.setText("");
    }
}
