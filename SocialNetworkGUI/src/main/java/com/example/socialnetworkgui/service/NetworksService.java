package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repository.memory.InMemoryRepository;
import com.example.socialnetworkgui.repository.memory.InMemoryRepositoryFriendship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetworksService {
    ///service retea
    private final InMemoryRepository<Integer, User> repositoryUsers;

    private final InMemoryRepositoryFriendship repositoryFriendships;

    public NetworksService(InMemoryRepository<Integer, User> repositoryUsers, InMemoryRepositoryFriendship repositoryFriendships) {
        this.repositoryUsers = repositoryUsers;
        this.repositoryFriendships = repositoryFriendships;
    }

    ///returneaza prietenii unui user intr-un set
    public Set<User> getFriendsUser(User user) {
        Set<User> friends = new HashSet<>();
        for (Friendship friendship : repositoryFriendships.getEntities()) {
            if (friendship.getIdUser1().equals(user.getId()))
                friends.add(repositoryUsers.getEntities().get(friendship.getIdUser2()));
            if (friendship.getIdUser2().equals(user.getId()))
                friends.add(repositoryUsers.getEntities().get(friendship.getIdUser1()));
        }
        return friends;
    }


    private int visit(User user,Set<User> visited,Set<User> network,int length){
        int len = length;
        network.add(user);
        visited.add(user);
        for(User friend: getFriendsUser(user)){
            if(!visited.contains(friend)){
                len++;
                len += visit(friend, visited, network, len);
            }
        }
        return len;
    }

    //gaseste toate comunitatiile cu ajutorul unui algoritm de tip dfs si le pune intr-o lista
    public List<Set<User>> networks(){
        Set<User> visited = new HashSet<>();
        List<Set<User>> networksSet = new ArrayList<>();
        for(User user: repositoryUsers.getEntities().values()){
            if(!visited.contains(user)){
                Set<User> newSet = new HashSet<>();
                networksSet.add(newSet);
                visit(user, visited, newSet, 0);
            }
        }
        return networksSet;
    }

    //gaseste cea mai lunga retea cu ajutorul unui dfs si o returneaza
    public Set<User> longestNetwork(){
        int len = 0;
        Set<User> longestNetworkSet = null;
        Set<User> visited = new HashSet<>();
        List<Set<User>> networksSet = new ArrayList<>();
        for(User user: repositoryUsers.getEntities().values()){
            if(!visited.contains(user)){
                Set<User> newSet = new HashSet<>();
                networksSet.add(newSet);
                int length = visit(user, visited, newSet, 0);
                if(length > len){
                    longestNetworkSet = newSet;
                    len = length;
                }
            }
        }
        return longestNetworkSet;
    }


}
