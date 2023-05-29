package com.example.socialnetworkgui.repository.db;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.Map;
import java.util.Objects;

public class UserDbRepository extends InMemoryRepository<Integer, User> {

    private String url;
    private String username;
    private String password;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        loadUsers();
    }

    public void loadUsers() {
        super.entities.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(id, firstName, lastName, email, password);
                super.save(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User addUser(User entity) {

        super.save(entity);
        String sql = "insert into users (id,first_name,last_name,email,password) values (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findOne(Integer id) {
        return super.findOne(id);
    }

    public User deleteUser(Integer id) {
        User user = super.delete(id);
        String sql = "delete from users where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Map<Integer, User> getUsers() {
        return super.getEntities();
    }

    public Integer sizeUsers() {
        return super.size();
    }

    public User updateUser(User userToUpdate, String newPassword) {
        User updatedUser = super.update(userToUpdate);
        String sql = "update users set password=? where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userToUpdate.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedUser;
    }

    public User findByEmailPassword(String email, String password){
        for(User user: getUsers().values())
            if(Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password))
                return user;
        return null;
    }

    public User findByName(String firstName,String lastName){
        for(User user: getUsers().values())
            if(Objects.equals(user.getFirstName(), firstName) && Objects.equals(user.getLastName(),lastName))
                return user;
        return null;
    }
}