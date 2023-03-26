package com.example.finalproject.DataClass;

public class User {
    private String fullName;
    private String email;

    public User(String fullName,  String email) {
        this.fullName = fullName;
        this.email = email;
    }
    private User(){

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
