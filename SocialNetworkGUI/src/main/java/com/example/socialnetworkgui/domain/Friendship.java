package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship {

    //o prietenie intre 2 utilizatori ai retelei de socializare
    private Integer idUser1;
    private Integer idUser2;

    private LocalDateTime friendsFrom;

    private FriendshipStatus friendshipStatus;

    public Friendship(Integer idUser1, Integer idUser2, LocalDateTime friendsFrom) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendsFrom = friendsFrom;
        this.friendshipStatus=FriendshipStatus.PENDING;
    }

    public Integer getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(Integer idUser1) {
        this.idUser1 = idUser1;
    }

    public Integer getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(Integer idUser2) {
        this.idUser2 = idUser2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }


    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public FriendshipStatus getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatusInt(Integer status)
    {
        if(status==1)
            this.friendshipStatus=FriendshipStatus.ACCEPTED;
        if(status==2)
            this.friendshipStatus=FriendshipStatus.DECLINED;
        if(status==3)
            this.friendshipStatus=FriendshipStatus.PENDING;
        if(status==4)
            this.friendshipStatus=FriendshipStatus.WITHDRAWN;
    }

    public Integer getFriendshipStatusInt(){
        if(this.friendshipStatus==FriendshipStatus.ACCEPTED)
            return 1;
        if(this.friendshipStatus==FriendshipStatus.DECLINED)
            return 2;
        if(this.friendshipStatus==FriendshipStatus.PENDING)
            return 3;
        if(this.friendshipStatus==FriendshipStatus.WITHDRAWN)
            return 4;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (idUser1.equals(that.idUser1) && idUser2.equals(that.idUser2) && friendsFrom.equals(that.friendsFrom)) || (idUser1.equals(that.idUser2) && idUser2.equals(that.idUser1) && friendsFrom.equals(that.friendsFrom));
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2);
    }

    public String formatFile() {
        return getIdUser1().toString() + ";" + getIdUser2().toString() + ";" + getFriendsFrom().toString();
    }

}
