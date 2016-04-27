package com.example.rydeldcosta.findme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Review_Activity extends AppCompatActivity {


    private Toolbar toolbar;
    restaurant_details thisrestaurant;
    User thisuser;
    ServerRequests serverRequests;
    private Restaurantlocalstore restaurantlocalstore;
    private Userlocalstore userlocalstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_);
        toolbar = (Toolbar) findViewById(R.id.feedbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText feedback = (EditText)findViewById(R.id.feedback);
        feedback.setCursorVisible(true);


    }
    public void pichejao(View view){
        EditText feedback = (EditText)findViewById(R.id.feedback);
        String review = feedback.getText().toString().trim();
        restaurantlocalstore = new Restaurantlocalstore(this);
        thisrestaurant = restaurantlocalstore.getRestdetails();

        userlocalstore = new Userlocalstore(this);
        thisuser = userlocalstore.getAllDetails();

        serverRequests = new ServerRequests(this);
        serverRequests.storeReview(thisuser.getId(), thisrestaurant.getrid(), review, new GetReviewCallBack() {
            @Override
            public void done() {
                startActivity(new Intent(Review_Activity.this,RestaurantAct.class));
            }

            @Override
            public void done(ReviewItem[] r,int reviews) {
                return;
            }
        });
    }
}
