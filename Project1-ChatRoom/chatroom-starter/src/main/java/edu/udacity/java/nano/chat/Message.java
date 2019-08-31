package edu.udacity.java.nano.chat;

/**
 * WebSocket message model
 */
public class Message {
    private String userName;
    private String message;
    private String messageType;
    private int onlineCount;

    public Message(){ }

    public Message(String userName, String message, String messageType, int onlineCount) {
        this.userName = userName;
        this.message = message;
        this.messageType = messageType;
        this.onlineCount = onlineCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}
