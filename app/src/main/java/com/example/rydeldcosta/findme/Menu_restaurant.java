package com.example.rydeldcosta.findme;

/**
 * Created by sudhanshu monga on 4/23/2016.
 */
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;


public class Menu_restaurant extends ActionBarActivity {

    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = (Toolbar) findViewById(R.id.menubar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void pichejao(View view){
        Intent i = new Intent(this , RestaurantAct.class);
        startActivity(i);
    }

}
