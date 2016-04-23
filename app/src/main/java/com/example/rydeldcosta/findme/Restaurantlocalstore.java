package com.example.rydeldcosta.findme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rydel Dcosta on 4/22/2016.
 */
public class Restaurantlocalstore {
    SharedPreferences sharedpref;
    public static final String SP_NAME = "RestDetails";


    public Restaurantlocalstore(Context context) {

        sharedpref = context.getSharedPreferences(SP_NAME,0);

    }

    public void storeRestaurant(restaurant_details thisrestaurant)
    {
        SharedPreferences.Editor spEditor = sharedpref.edit();
        spEditor.putInt("r_id",thisrestaurant.rid);
        spEditor.putString("r_name",thisrestaurant.name);
        spEditor.putString("r_tablename",thisrestaurant.table_name);
        spEditor.putString("contact",thisrestaurant.contact);
        spEditor.putInt("delivery",thisrestaurant.delivery);
        spEditor.apply();
    }
    public restaurant_details getRestdetails()
    {
        int r_id;
        String r_name;
        String r_tablename;
        String contact;
        int delivery;
        r_id = sharedpref.getInt("r_id", 0);
        r_name = sharedpref.getString("r_name", "");
        r_tablename = sharedpref.getString("r_tablename", "");
        contact = sharedpref.getString("contact", "");
        delivery = sharedpref.getInt("delivery", 0);

        return new restaurant_details(r_id,r_name,r_tablename,contact,delivery);
    }
    public void logOutRest() {
        SharedPreferences.Editor spEditor = sharedpref.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
