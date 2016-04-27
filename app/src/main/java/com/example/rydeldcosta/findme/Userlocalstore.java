package com.example.rydeldcosta.findme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rydel Dcosta on 4/21/2016.
 */
public class Userlocalstore {

    public static final String SP_NAME = "UserDetails";
    SharedPreferences sharedPreferences;

    public Userlocalstore(Context context){
        sharedPreferences = context.getSharedPreferences(SP_NAME,0);
    }
    public void storePersonal(User user) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putInt("id",user.getId());
        spEditor.putString("username", user.getUsername());
        spEditor.putString("name", user.getName());
        spEditor.putString("password", user.getPassword());
        spEditor.putString("email", user.getEmail());
        spEditor.apply();
    }

    public User getAllDetails() {
        String username;
        String name;
        String password;
        String email;
        int id;
        id = sharedPreferences.getInt("id",0);
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        name = sharedPreferences.getString("name", "");
        email = sharedPreferences.getString("email", "");

        return new User(id,name,username, password, email);
    }
    public void logOutUser() {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
