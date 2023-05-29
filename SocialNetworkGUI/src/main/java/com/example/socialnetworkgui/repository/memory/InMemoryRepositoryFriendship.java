package com.example.socialnetworkgui.repository.memory;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.FriendshipStatus;
import com.example.socialnetworkgui.domain.validators.ValidationException;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repository.db.FriendshipDbRepository;
import com.example.socialnetworkgui.repository.exceptions.RepositoryException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InMemoryRepositoryFriendship {

    ///Repository in memorie pentru prietenii
    private Validator<Friendship> validator;

    protected Set<Friendship> entities;

    public InMemoryRepositoryFriendship(Validator<Friendship> validator) {
        this.validator = validator;
        this.entities = new HashSet<>();
    }

    //returneaza un set cu toate prieteniile
    public Set<Friendship> getEntities() {
        return entities;
    }

    //date de intrare:O prietenie
    //date de iesire:nimic,daca adaugarea a fost facuta cu succes
    //               se arunca exceptie daca prietenia exista deja
    //               se arunca exceptie daca prietenia nu este valida
    public void add(Friendship friendship) {
        /*if (entities.contains(friendship))
            throw new RepositoryException("Prietenia exista deja! ");
        try {
            validator.validate(friendship);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return;
        }
        entities.add(friendship);*/
        try{
            validator.validate(friendship);
        }catch(ValidationException e){
            System.out.println(e.getMessage());
            return;
        }
        for(Friendship f:getEntities()){
            if(f.equals(friendship) && f.getFriendshipStatus()!=FriendshipStatus.DECLINED && f.getFriendshipStatus()!=FriendshipStatus.WITHDRAWN)
                throw new RepositoryException("Friendship already exists or a friend request was sent!");
        }
        if(!entities.add(friendship)){
            throw new RepositoryException("Friendship already exists!");
        }
    }

    //date de intrare:O prietenie
    //date de iesire:nimic,daca stergerea se fac cu succes
    //               se arunca exceptie daca prietenia pe care doriti sa o stergeti nu exista
    public void delete(Friendship friendship) {
        /*if (!entities.contains(friendship))
            throw new RepositoryException("Prietenia pe care doriti sa o stergeti nu exista! ");
        entities.remove(friendship);*/
        for(Friendship f:getEntities()){
            if(Objects.equals(f.getIdUser1(), friendship.getIdUser1()) && Objects.equals(f.getIdUser2(), friendship.getIdUser2()) && f.getFriendshipStatus()==FriendshipStatus.ACCEPTED) {
                entities.remove(f);
                return;
            }
            if(Objects.equals(f.getIdUser1(), friendship.getIdUser2()) && Objects.equals(f.getIdUser2(), friendship.getIdUser1()) && f.getFriendshipStatus()==FriendshipStatus.ACCEPTED) {
                entities.remove(f);
                return;
            }
        }
        throw new RepositoryException("Prietenia pe care doriti sa o stergeti nu exista! ");
    }

    /*
    public void update(Friendship friendshipToUpdate, User updatedUser){
        if(!entities.contains(friendshipToUpdate))
            throw new RepositoryException("Prietenia pe care doriti sa o updatati nu exista!");
        if(friendshipToUpdate.getUser1().equals(updatedUser))
        {
            friendshipToUpdate.setUser1(updatedUser);
            return;
        }
        if(friendshipToUpdate.getUser2().equals(updatedUser)){
            friendshipToUpdate.setUser2(updatedUser);
            return;
        }
    }*/

    //returneaza numarul de prietenii
    public Integer size() {
        return entities.size();
    }

    public void updateStatus(Friendship friendship,Integer status){
        for(Friendship f:getEntities()){
            if(Objects.equals(f.getIdUser1(), friendship.getIdUser1()) && Objects.equals(f.getIdUser2(), friendship.getIdUser2()) && f.getFriendsFrom()==friendship.getFriendsFrom()) {
                f.setFriendshipStatusInt(status);
                return;
            }
        }
        throw new RepositoryException("Prietenia pe care doriti sa o updatati nu exista! ");
    }
}
