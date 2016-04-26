package com.example.rydeldcosta.findme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

public class register extends AppCompatActivity {

    EditText etun, etpass, etmail, etage, retpass, etphone, etname;
    Button bSave;
    private CoordinatorLayout coordinatorLayout;
    private static final String REGISTER_URL = "http://www.grababite.com/register1.php";
    ServerRequests serverRequests;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        String fontpath = "Fonts/Sketch_Block.ttf";
        TextView name1 = (TextView) findViewById(R.id.name1);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontpath);
        name1.setTypeface(tf);
        serverRequests = new ServerRequests(this);
        Toolbar tool = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etun = (EditText) findViewById(R.id.etun);
        etpass = (EditText) findViewById(R.id.etpass);
        retpass = (EditText) findViewById(R.id.retpass);
        etmail = (EditText) findViewById(R.id.etmail);
        etage = (EditText) findViewById(R.id.etage);
        //   etphone = (EditText) findViewById(R.id.etphone);
        etname = (EditText) findViewById(R.id.etname);

        bSave = (Button) findViewById(R.id.bSave);

        etage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                           @Override
                                           public void onFocusChange(View v, boolean hasFocus) {
                                               String s1, m, f;
                                               s1 = etage.getText().toString();
                                               m = "M";
                                               f = "F";
                                               if (!hasFocus) {
                                                   if (s1.trim().equals("")) {
                                                       etage.setText("");
                                                       etage.setHint("Enter M/F");
                                                       etage.setHintTextColor(Color.GRAY);
                                                   } else if (!(s1.equals(m) || s1.equals(f))) {
                                                       etage.setText("");
                                                       etage.setHint("Enter M/F");
                                                       etage.setHintTextColor(Color.RED);
                                                   }
                                               }
                                           }
                                       }
        );

        etpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String temppass = etpass.getText().toString();
                    temppass = temppass.trim();
                    int l = temppass.length();
                    if (temppass.equals("")) {
                        etpass.setHint("Enter Password");
                        etpass.setHintTextColor(Color.rgb(114, 114, 114));
                    } else if (l < 5) {
                        etpass.setText("");
                        etpass.setHintTextColor(Color.RED);
                        etpass.setHint("Min 5 characters");
                    }
                }
            }
        });

        etmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email;
                    email = etmail.getText().toString();
                    if (etmail.getText().toString().equals("")) {
                        etmail.setHint("Enter Email ID");
                        etmail.setHintTextColor(Color.GRAY);
                    } else if (!isEmailValid(email)) {
                        etmail.setText("");
                        etmail.setHintTextColor(Color.RED);
                        etmail.setHint("Enter a valid Email ID");
                    }
                }
            }
        });


        retpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pass, repass;
                    repass = retpass.getText().toString();
                    pass = etpass.getText().toString();
                    if (retpass.getText().toString().equals("")) {
                        retpass.setHint("Re-enter Password");
                        retpass.setHintTextColor(Color.rgb(114, 114, 114));
                    } else if (!(pass.equals(repass))) {
                        retpass.setText("");
                        retpass.setHintTextColor(Color.RED);
                        retpass.setHint("Does not match entered Password");
                    }

                }
            }
        });


    }

    public void proceed(View v) {

        //   Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();


        etun = (EditText) findViewById(R.id.etun);
        etpass = (EditText) findViewById(R.id.etpass);
        retpass = (EditText) findViewById(R.id.retpass);
        etmail = (EditText) findViewById(R.id.etmail);
        etage = (EditText) findViewById(R.id.etage);
        //   etphone = (EditText) findViewById(R.id.etphone);
        etname = (EditText) findViewById(R.id.etname);

        String s1, m, f;
        s1 = etage.getText().toString();
        m = "M";
        f = "F";

        if (!(s1.equals(m) || s1.equals(f))) {
            etage.setText("");
            etage.setHint("Enter M/F");
            etage.setHintTextColor(Color.RED);
        }
        if (etage.getText().toString().trim().equals("") || etun.getText().toString().trim().equals("") || etpass.getText().toString().trim().equals("")
                || retpass.getText().toString().trim().equals("") || etname.getText().toString().trim().equals("") || etmail.getText().toString().trim().equals("")) {

            Snackbar snackbar = Snackbar
                    .make(v, "Some Fields are empty", Snackbar.LENGTH_LONG);
            snackbar.show();

        } else {

            user = new User(etname.getText().toString(),etun.getText().toString(),etpass.getText().toString(),etmail.getText().toString());
            serverRequests.storeUserDatainBackground(user, new GetUserCallBack() {
                @Override
                public void done(User returnedUser) {
                    startActivity(new Intent(register.this, login.class));

                }
            });
                //startActivity(new Intent(this, login.class));

        }
    }



    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }


}
