package com.example.rydeldcosta.findme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Rydel Dcosta on 4/18/2016.
 */
public class ServerRequests {
    private static final String SERVER_ADDRESS = "http://172.19.17.21/";
    private final int CONNECTION_TIMEOUT = 15 * 1000;
    private final String CONNECTION_ERROR = "Cannot connect to the server, please check your connection and try again";
    ProgressDialog progressDialog, progressDialog1;
    String serverresponse;
    Context context;
    Activity parentActivity;
    Restaurantlocalstore r;



    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        //userProfile = new UserProfile();
        this.context = context;
        //this.userLocalStore = new UserLocalStore(context);
        parentActivity = (Activity) context;
    }

    public String getStringfromContentValues(ContentValues dataToSend) {
        String newpostedData = "";
        String postedData = dataToSend.toString().trim();
        for (int i = 0; i < postedData.length(); i++) {
            if (postedData.charAt(i) == ' ' && postedData.charAt(i + 1) != '&')
                newpostedData += postedData.charAt(i);
            else if (postedData.charAt(i) == ' ' && postedData.charAt(++i) != '&') continue;
            else newpostedData += postedData.charAt(i);
        }
        return newpostedData;
    }
    public void positiveAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Successful!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void negativeAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Unsuccessful!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void fetchUserDatafromBackground(User user, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, getUserCallBack).execute();
    }
    public void fetchRestaurantDatafromBackground(String rest_name, GetRestaurantCallback getRestaurantCallback) {
        progressDialog.show();
        new fetchRestaurantDataAsyncTask(rest_name, getRestaurantCallback).execute();
    }
    private class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallBack getUserCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallBack getUserCallBack) {
            this.user = user;
            this.getUserCallback = getUserCallBack;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else {
                Toast toast = Toast.makeText(context, "Registered Successfully! Login to continue", Toast.LENGTH_LONG);
                toast.show();
            }
            getUserCallback.done(null);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&name", user.getName().trim());
            dataToSend.put("&username", user.getUsername().trim());
            dataToSend.put("&password", user.getPassword().trim());
            dataToSend.put("&email", user.getEmail().trim());
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "register.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    System.out.println("if condition");
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                    System.out.println("RESPONSE = " + serverresponse);
                } else {
                    System.out.println(responseCode);
                    System.out.println("else condition");
                    serverresponse = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack getUserCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallBack getUserCallBack) {
            this.user = user;
            this.getUserCallback = getUserCallBack;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else if (returnedUser == null) negativeAlert("Invalid username/password");
            getUserCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }

        @Override
        protected User doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&username", user.getUsername().trim());
            dataToSend.put("&password", user.getPassword().trim());
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            JSONObject jsonObject;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "login.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                System.out.println("Writing");
                writer.write(postedData);
                writer.flush();
                writer.close();
                System.out.println("Written");
                int responseCode = conn.getResponseCode();

                System.out.println(HttpURLConnection.HTTP_OK);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                    System.out.println(serverresponse);
                    jsonObject = new JSONObject(serverresponse);

                    if (jsonObject.length() == 0) return null;
                    else {
                        System.out.println("RESPONSE = " + serverresponse);//"+" + jsonObject.getString("com1name") + "+" + jsonObject.getString("com2name") + "+" + jsonObject.getString("com3name"));
                        return new User(getSpaces(jsonObject.getString("username")), getSpaces(jsonObject.getString("name")), getSpaces(jsonObject.getString("password")), getSpaces(jsonObject.getString("email")));
                    }
                } else serverresponse = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class fetchRestaurantDataAsyncTask extends AsyncTask<Void, Void, restaurant_details> {
        String rest_name;
        GetRestaurantCallback getRestaurantCallback;

        public fetchRestaurantDataAsyncTask(String rest_name, GetRestaurantCallback getRestaurantCallback) {
            this.rest_name = rest_name;
            this.getRestaurantCallback = getRestaurantCallback;
        }

        @Override
        protected void onPostExecute(restaurant_details restaurant) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else
            {System.out.println("onPost success");
                getRestaurantCallback.done(restaurant);


            }
            super.onPostExecute(restaurant);
        }

        @Override
        protected restaurant_details doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&rest_name", rest_name);

            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            JSONObject jsonObject;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "fetchRestaurant.php");

                //setup connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //setup posting the data
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                System.out.println("Writing");
                writer.write(postedData);
                writer.flush();
                writer.close();
                System.out.println("Written");
                int responseCode = conn.getResponseCode();

                System.out.println(HttpURLConnection.HTTP_OK);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        serverresponse += line;
                    }
                    System.out.println(serverresponse);
                    jsonObject = new JSONObject(serverresponse);

                    if (jsonObject.length() == 0) {System.out.println("length=0");return null;}
                    else {
                        System.out.println("RESPONSE = " + serverresponse);//"+" + jsonObject.getString("com1name") + "+" + jsonObject.getString("com2name") + "+" + jsonObject.getString("com3name"));
                        restaurant_details newr = new restaurant_details(getSpaces(jsonObject.getString("r_id")), getSpaces(jsonObject.getString("r_name")), getSpaces(jsonObject.getString("r_tablename")), getSpaces(jsonObject.getString("contact")), getSpaces(jsonObject.getString("delivery")));
                        //System.out.println(newr.rid+" "+newr.name+" "+newr.table_name+" "+newr.contact+" ");

                        //store new restaurant
                        r = new Restaurantlocalstore(context);
                        r.storeRestaurant(newr);
                        return newr;
                    }
                } else serverresponse = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    String getSpaces(String string) {
        string = string.trim();
        string = string.replace('+', ' ');
        return string;

    }
    public void storeUserDatainBackground(User user, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, getUserCallBack).execute();
    }

}
