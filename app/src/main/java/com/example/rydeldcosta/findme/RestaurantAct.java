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
public class RestaurantAct extends AppCompatActivity implements Serializable {

    private String m_Text = "";
    private ViewAdapter adapter ;//
    private RecyclerView rv ;//
    private FloatingActionButton fab;
    private Toolbar toolbar;
    restaurant_details thisrestaurant;
    ServerRequests serverRequests;
    Restaurantlocalstore restaurantlocalstore;
    ArrayList<com.example.rydeldcosta.findme.MenuItem> array_menu;
    ArrayList<ReviewItem> array_review;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MapsActivity.class));
    }

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

        setRecommendations();
        setReviews();

        //calling function

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+thisrestaurant.contact));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

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
                // Log.e("Calling a Phone Number", "Call failed", activityException);
                //}
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(),"Call Failed",Toast.LENGTH_SHORT).show();
                }
            }




        });


    }

    private void setRecommendations() {
        serverRequests = new ServerRequests(this);
        serverRequests.getRecommended(thisrestaurant.table_name, new GetMenuCallBack() {
            @Override
            public void done(com.example.rydeldcosta.findme.MenuItem[] menuItems) {
                TextView r1,r2,r3,r4,r5,r6;
                r1 = (TextView) findViewById(R.id.recname1);
                r2 = (TextView) findViewById(R.id.recprice1);
                r3 = (TextView) findViewById(R.id.recname2);
                r4 = (TextView) findViewById(R.id.recprice2);
                r5 = (TextView) findViewById(R.id.recname3);
                r6 = (TextView) findViewById(R.id.recprice3);
                r1.setText(menuItems[0].dish_name);
                r2.setText("Rs." +String.valueOf(menuItems[0].price));
                r3.setText(menuItems[1].dish_name);
                r4.setText("Rs." +String.valueOf(menuItems[1].price));
                r5.setText(menuItems[2].dish_name);
                r6.setText("Rs." +String.valueOf(menuItems[2].price));
            }
        });
    }

    private void setReviews() {
        serverRequests = new ServerRequests(this);
        serverRequests.getReviews(thisrestaurant.table_name, new GetReviewCallBack() {
            @Override
            public void done() {
                return;
            }

            @Override
            public void done(ReviewItem[] reviewItems, int reviews) {
                array_review = new ArrayList<ReviewItem>();
                if(reviews==0)
                {
                    ReviewItem r = new ReviewItem("Admin","User reviews will appear here");
                    array_review.add(0,r);
                }
                else
                {
                    for(int j=0;j<reviewItems.length;j++)
                    {
                        array_review.add(j,reviewItems[j]);
                    }

                }
                rv = (RecyclerView)findViewById(R.id.item_mode);//
                adapter = new ViewAdapter( RestaurantAct.this ,getData(array_review) );//
                rv.setAdapter(adapter);//
                rv.setLayoutManager(new LinearLayoutManager(RestaurantAct.this));//

            }
        });
    }

    public void GotoMenu(View view){
        final Intent i = new Intent(RestaurantAct.this , search_res.class);
        serverRequests = new ServerRequests(RestaurantAct.this);
        serverRequests.getMenu(thisrestaurant.table_name, new GetMenuCallBack() {
            @Override
            public void done(com.example.rydeldcosta.findme.MenuItem[] menuItems) {
                int j;
                array_menu = new ArrayList<com.example.rydeldcosta.findme.MenuItem>();
                for(j=0;j<menuItems.length;j++)
                {
                    System.out.println(menuItems[j].dish_name);
                    array_menu.add(j,menuItems[j]);
                }
                String s = "Menu";
                Bundle bundle = new Bundle();
                bundle.putString("id","menu");
                bundle.putString("key",thisrestaurant.getName() );
                bundle.putSerializable("searchmenu", (Serializable)array_menu);
                i.putExtra("BUNDLE",bundle);
                startActivity(i);
            }
        });
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
                if(m_Text.equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter a budget",Toast.LENGTH_SHORT).show();
                else
                    searchBudget(m_Text);
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

    private void searchBudget(final String m_text) {
        final Intent i = new Intent(RestaurantAct.this , search_res.class);
        serverRequests = new ServerRequests(RestaurantAct.this);
        serverRequests.searchBudget(Integer.parseInt(m_text.trim()), thisrestaurant.table_name, new GetMenuCallBack() {
            @Override
            public void done(com.example.rydeldcosta.findme.MenuItem[] menuItems) {
                int j;
                array_menu = new ArrayList<com.example.rydeldcosta.findme.MenuItem>();
                for(j=0;j<menuItems.length;j++)
                {
                    array_menu.add(j,menuItems[j]);
                }

                Bundle bundle = new Bundle();
                bundle.putString("id","budget");
                bundle.putString("key", (m_text.trim()));
                bundle.putSerializable("searchmenu", (Serializable)array_menu);
                i.putExtra("BUNDLE",bundle);
                startActivity(i);
            }
        });

    }

    public void reviewlikho(View view){
        Intent i = new Intent(this , Review_Activity.class);
        startActivity(i);
    }

    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){



        View layout = inflater.inflate(R.layout.activity_restaurant , container , false);



        return layout;


    }
    public static List<ReviewItem> getData(ArrayList<ReviewItem> reviews){//
        List<ReviewItem> data = new ArrayList<>();
        //int[] icons = {R.drawable.chef,R.drawable.chef,R.drawable.chef,R.drawable.chef, R.drawable.food1 , R.drawable.food2 , R.drawable.food3};
        //String[] titles = {thisrestaurant.contact ,thisrestaurant.contact ,thisrestaurant.contact ,thisrestaurant.contact ,thisrestaurant.table_name, String.valueOf(thisrestaurant.delivery), String.valueOf(thisrestaurant.rid)};
        for( ReviewItem r : reviews){
            ReviewItem current = new ReviewItem(r.name,r.review);

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
            public boolean onQueryTextSubmit(final String query) {
                // perform query here
                final Intent i = new Intent(RestaurantAct.this , search_res.class);
                serverRequests = new ServerRequests(RestaurantAct.this);
                serverRequests.searchMenu(query.trim(), thisrestaurant.table_name, new GetMenuCallBack() {
                    @Override
                    public void done(com.example.rydeldcosta.findme.MenuItem[] menuItems) {
                        int j;
                        array_menu = new ArrayList<com.example.rydeldcosta.findme.MenuItem>();
                        for(j=0;j<menuItems.length;j++)
                        {
                            array_menu.add(j,menuItems[j]);
                        }
                        //i.putExtra("sea",ArrayList< com.example.rydeldcosta.findme.MenuItem> array_menu);
                        //i.putCharSequenceArrayListExtra("searchmenu",ArrayList<MenuItem>array_menu);
                        Bundle bundle = new Bundle();
                        bundle.putString("id","search");
                        bundle.putString("key",query.trim());
                        bundle.putSerializable("searchmenu", (Serializable)array_menu);
                        i.putExtra("BUNDLE",bundle);
                        startActivity(i);
                    }
                });


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
