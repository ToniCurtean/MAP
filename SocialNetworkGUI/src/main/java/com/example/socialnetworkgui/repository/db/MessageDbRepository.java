package com.example.socialnetworkgui.repository.db;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.validators.MessageValidator;
import com.example.socialnetworkgui.domain.validators.ValidationException;
import com.example.socialnetworkgui.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MessageDbRepository {
    private List<Message> messages=new ArrayList<>();
    private String url;
    private String username;
    private String password;

    private Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        loadData();
    }

    public void loadData(){
        messages.clear();
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from messages order by message_time");
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer idUser1 = resultSet.getInt("id1");
                Integer idUser2=resultSet.getInt("id2");
                String text=resultSet.getString("message");
                LocalDateTime date=LocalDateTime.parse(resultSet.getString("message_time"));
                Message message=new Message(idUser1,idUser2,text,date);
                messages.add(message);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public void addMessage(Message message){
        messages.add(message);
        String sql = "insert into messages (id1,id2,message,message_time) values (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,message.getFrom());
            ps.setInt(2,message.getTo());
            ps.setString(3,message.getText());
            ps.setString(4,message.getTime().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessages(){
        loadData();
        return messages;
    }
}
