package com.example.rydeldcosta.findme;

import java.io.Serializable;

/**
 * Created by Rydel Dcosta on 4/26/2016.
 */
public class MenuItem implements Serializable{
    String dish_name;
    int price;
    public MenuItem (String s, int x){
        dish_name = s;
        price = x;
    }
}
