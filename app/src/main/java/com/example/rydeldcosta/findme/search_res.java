package com.example.rydeldcosta.findme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.rydeldcosta.findme.RestaurantAct.getData;

public class search_res extends AppCompatActivity {

    MenuItem item_detail;
    private vivzAdapter adap;
    private Toolbar toolbar;
    private RecyclerView rv_menu;
    ArrayList<MenuItem> menuItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_res);
        toolbar = (Toolbar) findViewById(R.id.searchbar);
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("BUNDLE");
        String id = b.getString("id");
        String key = b.getString("key");
        menuItems = (ArrayList<MenuItem>) b.getSerializable("searchmenu");
        rv_menu = (RecyclerView)findViewById(R.id.menu_mode);//
        adap = new vivzAdapter( this ,getData(menuItems) );//
        rv_menu.setAdapter(adap);//
        rv_menu.setLayoutManager(new LinearLayoutManager(this));

        if(id.equals("search"))
            toolbar.setTitle("Showing res for " + key);
        else if(id.equals("budget"))
            toolbar.setTitle("Foods under Rs." + key);
        else
            toolbar.setTitle(key);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public static List<menu_Information> getData(ArrayList<MenuItem>menuItems){//
        List<menu_Information> data = new ArrayList<>();
        for( MenuItem m : menuItems){
            menu_Information current = new menu_Information();
            current.Price = m.price;
            current.ItemName =  m.dish_name;
            data.add(current);
        }
        return data;
    }

}
