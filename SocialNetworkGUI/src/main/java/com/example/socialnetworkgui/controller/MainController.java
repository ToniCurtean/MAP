package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.Main;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repository.exceptions.RepositoryException;
import com.example.socialnetworkgui.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Service service;

    private User loggedUser;

    @FXML
    private Label userName;

    @FXML
    private Button acceptRequestButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button withdrawRequestButton;
    @FXML
    private Button sendRequestButton;

    @FXML
    private Button declineRequestButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField newFriend;

    @FXML
    private ChoiceBox<String> friendsBox;

    @FXML
    private ListView<String> friendsView;

    @FXML
    private ListView<String> requestsView;

    private final ObservableList<String> friends = FXCollections.observableArrayList();

    private final ObservableList<String> requests = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;

    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void init() {
        userName.setText("Welcome," + this.loggedUser.getFirstName() + " " + this.loggedUser.getLastName());
        friends.clear();
        friends.addAll(service.getFriends(getLoggedUser()));
        friendsView.setItems(friends);
        requests.clear();
        requests.addAll(service.getRequests(getLoggedUser()));
        requestsView.setItems(requests);
        friendsBox.setItems(friends);
        newFriend.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                try {
                    onSendRequestButtonClick(new ActionEvent());
                } catch (RepositoryException e) {

                }
        });

    }

    public void onSendRequestButtonClick(ActionEvent actionEvent) {
        if (newFriend.getText().strip().equals(""))
            return;
        try {
            String[] data = newFriend.getText().split(" ");
            User requestUser = service.getUserByName(data[0], data[1]);
            service.addFriendship(loggedUser.getId(), requestUser.getId());
            requests.clear();
            requests.addAll(service.getRequests(loggedUser));
            requestsView.setItems(requests);
            UIAlert.showMessage(null, Alert.AlertType.INFORMATION, "", "Friend request sent successfully");
        } catch (RepositoryException e) {
            UIAlert.showMessage(null, Alert.AlertType.INFORMATION, "", "Couldn't send friend request!");
        }
        newFriend.setText("");
    }

    public void onAcceptButtonClick(ActionEvent actionEvent) {
        if (requestsView.getSelectionModel().getSelectedItem() == null)
            return;
        String selected = requestsView.getSelectionModel().getSelectedItem();
        String[] data = selected.split(" ");
        User friend = service.getUserByName(data[0], data[1]);
        service.acceptRequest(friend.getId(), getLoggedUser().getId());
        requests.clear();
        requests.addAll(service.getRequests(getLoggedUser()));
        requestsView.setItems(requests);
        friends.clear();
        friends.addAll(service.getFriends(getLoggedUser()));
        friendsView.setItems(friends);
        friendsBox.setItems(friends);
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
        if (friendsView.getSelectionModel().getSelectedItem() == null)
            return;
        String selected = requestsView.getSelectionModel().getSelectedItem();
        String[] data = selected.split(" ");
        User friend = service.getUserByName(data[0], data[1]);
        try {
            service.deleteFriendship(friend.getId(), loggedUser.getId());
            friends.clear();
            friends.addAll(service.getFriends(getLoggedUser()));
            friendsView.setItems(friends);
            friendsBox.setItems(friends);
            UIAlert.showMessage(null, Alert.AlertType.INFORMATION, "", "Friendship deleted successfully");
        } catch (RepositoryException e) {
            UIAlert.showMessage(null, Alert.AlertType.INFORMATION, "", "Could not delete friendship!");
        }
    }

    public void onDeclineButtonClick(ActionEvent actionEvent) {
        if (requestsView.getSelectionModel().getSelectedItem() == null)
            return;
        String selected = requestsView.getSelectionModel().getSelectedItem();
        String[] data = selected.split(" ");
        User requestUser = service.getUserByName(data[0], data[1]);
        service.declineRequest(requestUser.getId(), loggedUser.getId());
        requests.clear();
        requests.addAll(service.getRequests(loggedUser));
        requestsView.setItems(requests);
    }

    public void onWithdrawButtonClick(ActionEvent actionEvent) {
        if(requestsView.getSelectionModel().getSelectedItem()==null)
            return;
        String selected=requestsView.getSelectionModel().getSelectedItem();
        String[] data=selected.split(" ");
        User requestUser=service.getUserByName(data[0],data[1]);
        service.withdrawRequest(loggedUser.getId(),requestUser.getId());
        requests.clear();
        requests.addAll(service.getRequests(loggedUser));
        requestsView.setItems(requests);
    }

    public void onSelectChoiceBox(MouseEvent event) throws IOException{
        if(friendsBox.getSelectionModel().getSelectedItem()==null)
            return;
        String selected=friendsBox.getSelectionModel().getSelectedItem();
        String[] data=selected.split(" ");
        User otherUser=service.getUserByName(data[0],data[1]);
        FXMLLoader loader=new FXMLLoader(Main.class.getResource("chat-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 360);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setTitle(data[0]+" "+data[1]);
        stage.show();
        ChatController chatController=loader.getController();
        chatController.setService(service);
        chatController.setLoggedUser(getLoggedUser());
        chatController.setOtherUser(otherUser);
        chatController.init();
    }

    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 360);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Welcome!");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();
        LoginController loginController = loader.getController();
        loginController.setService(service);
        loginController.init();
        ((Stage) logoutButton.getScene().getWindow()).close();
    }

}
