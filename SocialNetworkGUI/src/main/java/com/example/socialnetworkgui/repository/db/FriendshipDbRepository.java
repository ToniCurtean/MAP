package com.example.socialnetworkgui.repository.db;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repository.memory.InMemoryRepositoryFriendship;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Set;

public class FriendshipDbRepository extends InMemoryRepositoryFriendship {

    private String url;
    private String username;
    private String password;


    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        super(validator);
        this.url=url;
        this.username=username;
        this.password=password;
        loadFriendships();
    }

    public void loadFriendships(){
        super.entities.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer idUser1 = resultSet.getInt("id1");
                Integer idUser2=resultSet.getInt("id2");
                LocalDateTime friendsFrom=LocalDateTime.parse(resultSet.getString("friends_from"));
                Integer status=resultSet.getInt("status");
                Friendship friendship=new Friendship(idUser1,idUser2,friendsFrom);
                friendship.setFriendshipStatusInt(status);
                super.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFriendship(Friendship friendship) {

        super.add(friendship);
        String sql = "insert into friendships (id1,id2,friends_from,status) values (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,friendship.getIdUser1());
            ps.setInt(2,friendship.getIdUser2());
            ps.setString(3,friendship.getFriendsFrom().toString());
            ps.setInt(4,friendship.getFriendshipStatusInt());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFriendship(Friendship friendship){
        super.delete(friendship);
        String sql = "delete from friendships where (id1=? and id2=?) or (id1=? and id2=?) and status=ACCEPTED";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, friendship.getIdUser1());
            ps.setInt(2,friendship.getIdUser2());
            ps.setInt(3,friendship.getIdUser2());
            ps.setInt(4,friendship.getIdUser1());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFriendshipStatus(Friendship friendship,Integer status){
        super.updateStatus(friendship,status);
        String sql="update friendships set status=? where id1=? and id2=? and friends_from=?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setInt(1,status);
            ps.setInt(2, friendship.getIdUser1());
            ps.setInt(3,friendship.getIdUser2());
            ps.setString(4,friendship.getFriendsFrom().toString());
            ps.executeUpdate();
        }catch(SQLException e){
                e.printStackTrace();
        }
    }
    public Integer sizeFriendships() {
        return super.size();
    }

    public Set<Friendship> getFriendships(){
        loadFriendships();
        return super.getEntities();
    }

}

