package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.FriendshipStatus;
import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repository.db.FriendshipDbRepository;
import com.example.socialnetworkgui.repository.db.MessageDbRepository;
import com.example.socialnetworkgui.repository.db.UserDbRepository;
import com.example.socialnetworkgui.repository.exceptions.RepositoryException;

import java.time.LocalDateTime;
import java.util.*;

public class Service {

    private final UserDbRepository repositoryUsers;

    private final FriendshipDbRepository repositoryFriendships;

    private final NetworksService networksService;

    private final MessageDbRepository messageRepository;

    public Service(UserDbRepository repositoryUsers, FriendshipDbRepository repositoryFriendships, MessageDbRepository messageRepository) {
        this.repositoryUsers = repositoryUsers;
        this.repositoryFriendships = repositoryFriendships;
        this.messageRepository = messageRepository;
        networksService=new NetworksService(repositoryUsers,repositoryFriendships);
    }

    ///date de intrare:Long id,String firstName,String lastName-datele necesare pentru crearea unui utilizator
    //date de iesire:nimic,daca adaugarea a fost facuta cu succes
    //               exceptie daca datele folosite la crearea utilizatorului sunt invalide
    //               exceptie daca utilizatorul pe care doriti sa il adaugati exista deja in retea
    public void addUser(Integer id, String firstName, String lastName,String email,String password) {
        User user = new User(id, firstName, lastName, email, password);
        try {
            repositoryUsers.addUser(user);
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    //date de intrare:Long id-ID-ul unui utilizator
    //date de iesire:returneaza utilizatorul sters,daca stergerea a fost facuta cu succes
    //               exceptie,daca utilizatorul pe care doriti sa il stergeti nu exista
    public User deleteUser(Integer id) {
        try {
            User user = repositoryUsers.deleteUser(id);
            deleteAllFriendshipsUser(user);
            return user;
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public User updateUser(Integer id,String newPassword){
        try{
            repositoryUsers.findOne(id);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
        User userToUpdate=new User(repositoryUsers.findOne(id).getId(),repositoryUsers.findOne(id).getFirstName(),repositoryUsers.findOne(id).getLastName(),repositoryUsers.findOne(id).getEmail(),repositoryUsers.findOne(id).getPassword());
        try {
            User userUpdated=repositoryUsers.updateUser(userToUpdate, newPassword);
            return userUpdated;
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void addFriendship(Integer id1, Integer id2) {
        try{
            repositoryUsers.findOne(id1);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return;
        }
        try{
            repositoryUsers.findOne(id2);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return;
        }
        Friendship friendship = new Friendship(id1, id2, LocalDateTime.now());
        repositoryFriendships.addFriendship(friendship);
    }

    public void deleteFriendship(Integer id1, Integer id2) {

        try{
            repositoryUsers.findOne(id1);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return;
        }
        try{
            repositoryUsers.findOne(id2);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return;
        }
        Friendship friendship = new Friendship(id1, id2,LocalDateTime.now());
        repositoryFriendships.deleteFriendship(friendship);
    }

    public void deleteAllFriendshipsUser(User user) {
        List<Friendship> friendshipsUser = new ArrayList<>();
        for (Friendship friendship : repositoryFriendships.getEntities())
            if (friendship.getIdUser1().equals(user.getId()) || friendship.getIdUser2().equals(user.getId()))
                friendshipsUser.add(friendship);
        for (Friendship friendship : friendshipsUser)
            repositoryFriendships.deleteFriendship(friendship);
    }

    public Map<Integer, User> getAllUsers() {
        return repositoryUsers.getUsers();
    }

    public Set<Friendship> getFriendships() {
        return repositoryFriendships.getFriendships();
    }

    public Integer sizeUsers() {
        return repositoryUsers.sizeUsers();
    }

    public Integer sizeFriendships() {
        return repositoryFriendships.sizeFriendships();
    }

    public List<Set<User>> getNetworks(){
        return networksService.networks();
    }

    public Set<User> getLongestNetwork(){
        return networksService.longestNetwork();
    }

    public boolean checkLogin(String email, String password) {
        User user=repositoryUsers.findByEmailPassword(email,password);
        return user != null;
    }

    public User getUserByEmail(String email){
        for(User user:getAllUsers().values())
            if(Objects.equals(user.getEmail(), email))
                return user;
        return null;
    }

    public Set<String> getFriends(User loggedUser) {
        Set<String> friends=new HashSet<>();
        for(Friendship f:repositoryFriendships.getFriendships()){
            if(f.getFriendshipStatus()==FriendshipStatus.ACCEPTED && Objects.equals(f.getIdUser1(), loggedUser.getId())){
                User friend=repositoryUsers.findOne(f.getIdUser2());
                friends.add(friend.getFirstName()+" "+friend.getLastName()+" "+ f.getFriendsFrom());
            }
            if(f.getFriendshipStatus()==FriendshipStatus.ACCEPTED && Objects.equals(f.getIdUser2(), loggedUser.getId())){
                User friend=repositoryUsers.findOne(f.getIdUser1());
                friends.add(friend.getFirstName()+" "+friend.getLastName()+" "+f.getFriendsFrom());
            }
        }
        return friends;
    }

    public Set<String> getRequests(User loggedUser){
        Set<String> requests=new HashSet<>();
        for(Friendship f: repositoryFriendships.getFriendships()){
            if(f.getFriendshipStatus()==FriendshipStatus.PENDING && Objects.equals(f.getIdUser2(), loggedUser.getId())) {
                User userRequest=repositoryUsers.findOne(f.getIdUser1());
                requests.add(userRequest.getFirstName()+" "+userRequest.getLastName()+" "+f.getFriendsFrom());
            }
            if(f.getFriendshipStatus()==FriendshipStatus.PENDING && Objects.equals(f.getIdUser1(),loggedUser.getId())){
                User userRequest=repositoryUsers.findOne(f.getIdUser2());
                requests.add(userRequest.getFirstName()+" "+userRequest.getLastName()+" "+f.getFriendsFrom());
            }
        }
        return requests;
    }

    public User getUserByName(String firstName, String lastName) {
        return repositoryUsers.findByName(firstName,lastName);
    }

    public void acceptRequest(Integer friend, Integer loggedUser) {
        for(Friendship f:repositoryFriendships.getFriendships()){
            if(f.getFriendshipStatus()==FriendshipStatus.PENDING && Objects.equals(f.getIdUser1(), friend) && Objects.equals(f.getIdUser2(), loggedUser)){
                repositoryFriendships.updateFriendshipStatus(f,1);
                return;
            }
        }
    }

    public void declineRequest(Integer requestUser, Integer loggedUser){
        for(Friendship f:repositoryFriendships.getFriendships()){
            if(f.getFriendshipStatus()==FriendshipStatus.PENDING && Objects.equals(f.getIdUser1(),requestUser) && Objects.equals(f.getIdUser2(),loggedUser)){
                repositoryFriendships.updateFriendshipStatus(f,2);
            }
        }
    }

    public void withdrawRequest(Integer loggedUser, Integer requestUser) {
        for(Friendship f: repositoryFriendships.getFriendships()){
            if(f.getFriendshipStatus()==FriendshipStatus.PENDING && Objects.equals(f.getIdUser1(),loggedUser) && Objects.equals(f.getIdUser2(),requestUser))
                repositoryFriendships.updateFriendshipStatus(f,4);
        }
    }

    public void addMessage(Integer id1, Integer id2, String text) {
        Message message=new Message(id1,id2,text,LocalDateTime.now());
        messageRepository.addMessage(message);

    }

    public List<String> getMessages(Integer id1,Integer id2){
        List<String> messages=new ArrayList<>();
        for(Message m: messageRepository.getMessages()){
            if((Objects.equals(m.getFrom(), id1) && Objects.equals(m.getTo(), id2))){
                User from=repositoryUsers.findOne(id1);
                messages.add(from.getFirstName()+": "+m.getText());
            }
            if((Objects.equals(m.getFrom(),id2)&& Objects.equals(m.getTo(),id1))){
                User from=repositoryUsers.findOne(id2);
                messages.add(from.getFirstName()+": "+m.getText());
            }
        }
        return messages;
    }
}
