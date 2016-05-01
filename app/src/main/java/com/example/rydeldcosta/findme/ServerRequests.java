package com.example.rydeldcosta.findme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
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
    public void noItems() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Oops")
                .setMessage("Sorry no items match your keyword")
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
    public void searchMenu(String searchTerm, String table_name,GetMenuCallBack getMenuCallBack) {
        progressDialog.show();
        new searchMenuAsync(searchTerm, table_name,getMenuCallBack).execute();
    }
    public void getMenu(String table_name,GetMenuCallBack getMenuCallBack) {
        progressDialog.show();
        new getMenuAsync(table_name,getMenuCallBack).execute();
    }
    public void getRecommended(String table_name,GetMenuCallBack getMenuCallBack) {
        progressDialog.show();
        new getRecAsync(table_name,getMenuCallBack).execute();
    }
    public void searchBudget(int budget, String table_name,GetMenuCallBack getMenuCallBack) {
        progressDialog.show();
        new searchBudgetAsync(budget, table_name,getMenuCallBack).execute();
    }
    public void storeUserDatainBackground(User user, GetUserCallBack getUserCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, getUserCallBack).execute();
    }
    public void storeReview(int uid,int rid,String review, GetReviewCallBack getReviewCallBack) {
        progressDialog.show();
        new StoreReviewAsyncTask(uid,rid,review, getReviewCallBack).execute();
    }
    public void getReviews(String table_name, GetReviewCallBack getReviewCallBack) {
        progressDialog.show();
        new getReviewsAsyncTask(table_name, getReviewCallBack).execute();
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
            else if(serverresponse.equals("username exists?>")){
                Toast toast = Toast.makeText(context, "Username already exists", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(context, "Registered Successfully! Login to continue", Toast.LENGTH_LONG);
                toast.show();
                getUserCallback.done(null);
            }

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
    private class StoreReviewAsyncTask extends AsyncTask<Void, Void, Void> {
       int uid,rid;
        String review;
        GetReviewCallBack getReviewCallBack;

        public StoreReviewAsyncTask(int uid,int rid,String review,GetReviewCallBack getReviewCallBack) {
            this.uid = uid;
            this.rid = rid;
            this.review = review;
            this.getReviewCallBack = getReviewCallBack;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (serverresponse.equals("")) negativeAlert(CONNECTION_ERROR);
            else if(serverresponse.equals("error?>"))
            {
                Toast toast = Toast.makeText(context, "We have already recorded your feedback", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(context, "We value your feedback! Thankyou", Toast.LENGTH_LONG);
                toast.show();
            }
            getReviewCallBack.done();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&uid", uid);
            dataToSend.put("&rid", rid);
            dataToSend.put("&review", review);

            String postedData = getStringfromContentValues(dataToSend);
            System.out.println(postedData);
            URL url;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "insertReview.php");

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
                        return new User(Integer.parseInt(getSpaces(jsonObject.getString("id"))),getSpaces(jsonObject.getString("username")), getSpaces(jsonObject.getString("name")), getSpaces(jsonObject.getString("password")), getSpaces(jsonObject.getString("email")));
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
    private class getReviewsAsyncTask extends AsyncTask<Void, Void, ReviewItem[]> {
        String table_name;
        GetReviewCallBack getReviewCallBack;
        ReviewItem[] returnedReviews;

        getReviewsAsyncTask(String table_name,GetReviewCallBack getReviewCallBack) {

            this.getReviewCallBack = getReviewCallBack;
            this.table_name = table_name;
        }

        @Override
        protected ReviewItem[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            //dataToSend.put("&dish", searchTerm);
            dataToSend.put("&table",table_name);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "getReviews.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                if(jsonArray.length()==0){
                    serverresponse = "";
                    returnedReviews[0]=null;
                    return returnedReviews;
                }
                final int size = jsonArray.length();
                returnedReviews = new ReviewItem[size];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    returnedReviews[i] = new ReviewItem(jsonObject.getString("name"), (jsonObject.getString("review")));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedReviews;
        }

        @Override
        protected void onPostExecute(ReviewItem[] reviewItems) {
            progressDialog.dismiss();
            if(serverresponse.equals(""))
            {
                getReviewCallBack.done(reviewItems,0);
                return;
            }
            getReviewCallBack.done(reviewItems,1);
            super.onPostExecute(reviewItems);
        }
    }

    private class searchMenuAsync extends AsyncTask<Void, Void, MenuItem[]> {
        String searchTerm,table_name;
        GetMenuCallBack getMenuCallBack;
        MenuItem[] returnedItems;

        searchMenuAsync(String searchterm, String table_name,GetMenuCallBack getMenuCallBack) {
            this.searchTerm = searchterm;
            this.getMenuCallBack = getMenuCallBack;
            this.table_name = table_name;
        }

        @Override
        protected MenuItem[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&dish", searchTerm);
            dataToSend.put("&table",table_name);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "search.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                if(jsonArray.length()==0){
                    serverresponse = "";
                    returnedItems[0]=null;
                    return returnedItems;
                }
                final int size = jsonArray.length();
                returnedItems = new MenuItem[size];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    returnedItems[i] = new MenuItem(jsonObject.getString("dish_name"), Integer.parseInt(jsonObject.getString("dish_price")));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedItems;
        }

        @Override
        protected void onPostExecute(MenuItem[] menuItems) {
            progressDialog.dismiss();
            if(serverresponse.equals(""))
            {
                noItems();
                return;
            }
            getMenuCallBack.done(menuItems);
            super.onPostExecute(menuItems);
        }
    }
    private class searchBudgetAsync extends AsyncTask<Void, Void, MenuItem[]> {
        int budget;
        String table_name;
        GetMenuCallBack getMenuCallBack;
        MenuItem[] returnedItems;

        public searchBudgetAsync(int budget, String table_name,GetMenuCallBack getMenuCallBack) {
            this.budget = budget;
            this.getMenuCallBack = getMenuCallBack;
            this.table_name = table_name;
        }

        @Override
        protected MenuItem[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            dataToSend.put("&budget", budget);
            dataToSend.put("&table",table_name);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "budget.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                if(jsonArray.length()==0){
                    serverresponse = "";
                    returnedItems[0]=null;
                    return returnedItems;
                }
                final int size = jsonArray.length();
                returnedItems = new MenuItem[size];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    returnedItems[i] = new MenuItem(jsonObject.getString("dish_name"), Integer.parseInt(jsonObject.getString("dish_price")));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedItems;
        }

        @Override
        protected void onPostExecute(MenuItem[] menuItems) {
            progressDialog.dismiss();
            if(serverresponse.equals(""))
            {
                noItems();
                return;
            }
            getMenuCallBack.done(menuItems);
            super.onPostExecute(menuItems);
        }
    }
    private class getMenuAsync extends AsyncTask<Void, Void, MenuItem[]> {

        String table_name;
        GetMenuCallBack getMenuCallBack;
        MenuItem[] returnedItems;

        public getMenuAsync(String table_name,GetMenuCallBack getMenuCallBack) {

            this.getMenuCallBack = getMenuCallBack;
            this.table_name = table_name;
        }

        @Override
        protected MenuItem[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            //dataToSend.put("&budget", budget);
            dataToSend.put("&table",table_name);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "getmenu.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                if(jsonArray.length()==0){
                    serverresponse = "";
                    returnedItems[0]=null;
                    return returnedItems;
                }
                final int size = jsonArray.length();
                returnedItems = new MenuItem[size];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    returnedItems[i] = new MenuItem(jsonObject.getString("dish_name"), Integer.parseInt(jsonObject.getString("dish_price")));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedItems;
        }

        @Override
        protected void onPostExecute(MenuItem[] menuItems) {
            progressDialog.dismiss();
            if(serverresponse.equals(""))
            {
                noItems();
                return;
            }
            getMenuCallBack.done(menuItems);
            super.onPostExecute(menuItems);
        }
    }
    private class getRecAsync extends AsyncTask<Void, Void, MenuItem[]> {

        String table_name;
        GetMenuCallBack getMenuCallBack;
        MenuItem[] returnedItems;

        public getRecAsync(String table_name,GetMenuCallBack getMenuCallBack) {

            this.getMenuCallBack = getMenuCallBack;
            this.table_name = table_name;
        }

        @Override
        protected MenuItem[] doInBackground(Void... params) {
            ContentValues dataToSend = new ContentValues();
            //dataToSend.put("&budget", budget);
            dataToSend.put("&table",table_name);
            String postedData = getStringfromContentValues(dataToSend);
            System.out.println("PostedData = " + postedData);
            URL url;
            JSONObject jsonObject;
            getSpaces(postedData);
            JSONArray jsonArray;
            serverresponse = "";
            try {
                url = new URL(SERVER_ADDRESS + "recommendations.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(postedData);
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    while ((line = br.readLine()) != null)
                        serverresponse += line;
                }
                System.out.println("RESPONSE = " + serverresponse);
                jsonArray = new JSONArray(serverresponse);
                if(jsonArray.length()==0){
                    serverresponse = "";
                    returnedItems[0]=null;
                    return returnedItems;
                }
                final int size = jsonArray.length();
                returnedItems = new MenuItem[size];
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null) break;
                    returnedItems[i] = new MenuItem(jsonObject.getString("dish_name"), Integer.parseInt(jsonObject.getString("dish_price")));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedItems;
        }

        @Override
        protected void onPostExecute(MenuItem[] menuItems) {
            progressDialog.dismiss();
            if(serverresponse.equals(""))
            {
                noItems();
                return;
            }
            getMenuCallBack.done(menuItems);
            super.onPostExecute(menuItems);
        }
    }

    String getSpaces(String string) {
        string = string.trim();
        string = string.replace('+', ' ');
        return string;

    }


}
