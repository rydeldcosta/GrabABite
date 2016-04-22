package com.example.rydeldcosta.findme;

/**
 * Created by Rydel Dcosta on 4/18/2016.
 */
public class User {
    int id;
    String name,username,email,password;

    public User(String name, String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    public User(int id , String name, String username, String password, String email) {
        this.username = username;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    //for login
    public User(String username, String password) {
        this.username = username;

        this.password = password;

    }
    public int getId(){ return this.id; }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
}
