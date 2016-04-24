package com.example.rydeldcosta.findme;

import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class RestaurantAct extends AppCompatActivity {

    private String m_Text = "";
    private ViewAdapter adapter ;
    private RecyclerView rv ;
    private Button call_button;
    private Toolbar toolbar;
    restaurant_details thisrestaurant;
    ServerRequests serverRequests;
    Restaurantlocalstore restaurantlocalstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        final Bundle bun = getIntent().getExtras();

        //creating thisrestaurant object
        restaurantlocalstore = new Restaurantlocalstore(this);

        thisrestaurant = restaurantlocalstore.getRestdetails();
        //System.out.println(thisrestaurant.getName()+"M"+thisrestaurant.name);
        toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle(thisrestaurant.getName());
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        rv = (RecyclerView)findViewById(R.id.item_mode);
        adapter = new ViewAdapter( this ,getData(thisrestaurant) );
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }
    public void GotoMenu(View view){
        Intent i = new Intent(this , Menu_restaurant.class);
        startActivity(i);
    }

    public void popup(View view)
    {

        final float scale = getResources().getDisplayMetrics().density;
        int padding_5dp = (int) (5 * scale + 0.5f);
        int padding_20dp = (int) (20 * scale + 0.5f);
        int padding_50dp = (int) (50 * scale + 0.5f);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Budget");

// Set up the input
        final EditText input = new EditText(this);

        input.setLayoutParams(new FrameLayout.LayoutParams(padding_50dp,padding_50dp));
        input.setPadding(padding_20dp,padding_5dp,padding_5dp,padding_5dp);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        input.setBackgroundResource(R.drawable.budget);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void reviewlikho(View view){
        Intent i = new Intent(this , Review_Activity.class);
        startActivity(i);
    }

    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){



        View layout = inflater.inflate(R.layout.activity_restaurant , container , false);



        return layout;


    }
    public static List<Information> getData(restaurant_details thisrestaurant){
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.chef, R.drawable.food1 , R.drawable.food2 , R.drawable.food3};
        String[] titles = {thisrestaurant.contact ,thisrestaurant.table_name, String.valueOf(thisrestaurant.delivery), String.valueOf(thisrestaurant.rid)};
        for( int i =0; i < icons.length && i< titles.length ; i++){
            Information current = new Information();
            current.IconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Chicken, Parantha, Roll...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(RestaurantAct.this , search_res.class);
                startActivity(i);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
