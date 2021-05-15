package com.hopeTheRobot.pojo;

public class ChatUser {
    private String userUid;
    private String lastMessage;
    private Long lastMessageDate;
    private String userName;
    private String userPhoto;
    private Boolean seen;

    public ChatUser() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
