package com.example.rydeldcosta.findme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.rydeldcosta.findme.RestaurantAct.getData;

public class search_res extends AppCompatActivity {

    MenuItem item_detail;
    private vivzAdapter adap;
    private Toolbar toolbar;
    private FloatingActionButton fab1;
    private RecyclerView rv_menu;
    ArrayList<MenuItem> menuItems;
    restaurant_details thisrestaurant;
    private Restaurantlocalstore restaurantlocalstore;
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

        restaurantlocalstore = new Restaurantlocalstore(this);
        thisrestaurant = restaurantlocalstore.getRestdetails();


        //calling function
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" +thisrestaurant.contact));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                } //catch (ActivityNotFoundException activityException) {
                //Log.e("Calling a Phone Number", "Call failed", activityException);
                //}
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(),"Call Failed",Toast.LENGTH_SHORT).show();
                }
            }



        });


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
