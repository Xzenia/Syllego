package test.omegaware.syllego;

import java.util.ArrayList;

public class User {

    private int userId;
    private int bookListId;
    private String username;
    private String passwordHash;
    private String fullName;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {

        this.passwordHash = passwordHash;
    }

    public int getBookListId() {
        return bookListId;
    }

    public void setBookListId(int bookListId) {
        this.bookListId = bookListId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
