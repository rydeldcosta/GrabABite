package com.example.rydeldcosta.findme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,Serializable {

    private GoogleMap mMap;
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    TextView header_uname,header_email;
    Userlocalstore userlocalstore;
    User thisuser;
    private ServerRequests serverRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        //current user
        userlocalstore = new Userlocalstore(this);
        thisuser = userlocalstore.getAllDetails();

        //header
        View header = navigationView.getHeaderView(0);
        header_uname = (TextView) header.findViewById(R.id.header_un);
        header_email = (TextView) header.findViewById(R.id.header_em);
        header_uname.setText(thisuser.getUsername());
        header_email.setText(thisuser.getEmail());


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {




                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.about:
                        Intent i = new Intent(MapsActivity.this, aboutActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.credits:
                        Intent l = new Intent(MapsActivity.this, creditsActivity.class);
                        startActivity(l);
                        return true;
                    case R.id.contact:
                        Intent j = new Intent(MapsActivity.this, contactActivity.class);
                        startActivity(j);
                        return true;
                    case R.id.logout:
                       userlocalstore.logOutUser();
                        Intent k = new Intent(MapsActivity.this, login.class);
                        startActivity(k);
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();



    }

    public void onBite(View v) {
        TextView tx = (TextView) findViewById(R.id.mytext);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String t1 = String.valueOf(c.get(Calendar.HOUR)) + "am", t2 = String.valueOf(c.get(Calendar.HOUR)) + "pm";
        if (tx.getText().toString().equals(t1) ||tx.getText().toString().equals(t2)  ) {
            Context context = getApplicationContext();
            CharSequence text = "Please Select a restaurant!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            //Restaurantlocalstore r = new Restaurantlocalstore(this);
            final Intent i = new Intent(this, RestaurantAct.class);
            String msg = tx.getText().toString();

            serverRequests = new ServerRequests(this);
            serverRequests.fetchRestaurantDatafromBackground(msg, new GetRestaurantCallback() {
                        @Override
                        public void done(restaurant_details restaurant) {

                            {
                                System.out.println("before obj");
                                startActivity(i);
                                System.out.println("after obj");
                            }
                        }
                    }
            );


        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Marker source;

        mMap = googleMap;

        int[] time = {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1};

        ArrayList<restaurant_details> restList = new ArrayList<>(7);
        restList.add(new restaurant_details("Malik Dhaba", 25.428190, 81.770655, time));
        int[] time1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0};
        restList.add(new restaurant_details("Sanskar", 25.428748, 81.770461, time1));
        int[] time2 = {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1};

        restList.add(new restaurant_details("Sonu NC", 25.4272, 81.7718, time2));
        int[] time3 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0};

        restList.add(new restaurant_details("Punjabi Dhaba", 25.4303, 81.7675, time3));
        int[] time4 = {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1};

        restList.add(new restaurant_details("Ranu NC", 25.4278, 81.7714, time4));
        int[] time5 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0};

        restList.add(new restaurant_details("Cafeteria", 25.431234, 81.771908, time5));
        int[] time6 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0};

        //restList.add(new restaurant_details("OC", 25.428724, 81.7723, time6));

        setmarkers(restList);
        mMap.setOnMarkerClickListener(this);
        LatLngBounds IIITA = new LatLngBounds(
                new LatLng(25.429173, 81.765801), new LatLng(25.430817, 81.777005));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(IIITA.getCenter(), 15));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        else mMap.setMyLocationEnabled(true);
        //sets camera to my location


    }

    public void setmarkers(ArrayList<restaurant_details> restList) {


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        TextView mytext = (TextView) findViewById(R.id.mytext);

        //SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        //String formattedDate = df.format(c.getTime());
        if(c.get(Calendar.HOUR_OF_DAY) > 12)
            mytext.setText(String.valueOf(c.get(Calendar.HOUR)) + "pm");
        else
            mytext.setText(String.valueOf(c.get(Calendar.HOUR)) + "am");
        toolbar.setTitle("Restaurants open around you");
        for (restaurant_details obj : restList) {
            if (obj.time[hour] == 1) {
                LatLng OC = new LatLng(obj.xloc, obj.yloc);
                mMap.addMarker(new MarkerOptions().position(OC).title(obj.name));
            }
        }
    }

    @Override

    public boolean onMarkerClick(Marker marker) {
        TextView mytext = (TextView) findViewById(R.id.mytext);
        mytext.setText(marker.getTitle());
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
