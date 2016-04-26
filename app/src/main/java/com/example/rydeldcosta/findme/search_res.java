package com.example.rydeldcosta.findme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

public class search_res extends AppCompatActivity {

    private Toolbar toolbar;
    ArrayList<MenuItem> menuItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_res);
        toolbar = (Toolbar) findViewById(R.id.resbar);


        Intent i = getIntent();
        Bundle b = i.getBundleExtra("BUNDLE");
        menuItems = (ArrayList<MenuItem>) b.getSerializable("searchmenu");
        //TextView menu_tex = (TextView) findViewById(R.id.menu);
        String str = "";
        for(MenuItem m : menuItems)
        {
            str.concat(m.dish_name+" "+m.price+"\n");
        }
        toolbar.setTitle(str);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // menu_tex.setText(str);
    }
}
