package com.example.rydeldcosta.findme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.graphics.Typeface;

public class login extends AppCompatActivity {

    int count = 0;
    ServerRequests serverRequests;
    private EditText inputName, inputEmail, inputPassword;
    private TextInputLayout inputLayoutName,  inputLayoutPassword;
    private Button btnSignUp;
    private TextView err_msg_password;
    Userlocalstore userlocalstore;
    //private Userlocalstore userLocalStore;

    public void register(View v) {
        Intent intent = new Intent(login.this, register.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userlocalstore = new Userlocalstore(this);
        if (authenticateifLoggedIn()) {
            startActivity(new Intent(this, MapsActivity.class));
        }

        String fontpath = "Fonts/Sketch_Block.ttf";
        TextView name = (TextView) findViewById(R.id.name);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontpath);
        name.setTypeface(tf);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputName = (EditText) findViewById(R.id.input_name);
        //  inputName.setHintTextColor(Color.WHITE);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));

        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });




    }

    private boolean authenticateifLoggedIn() {
        User user = userlocalstore.getAllDetails();
        System.out.println("Got " + user.getUsername());
        return (user.getUsername() != null && (!user.getUsername().equals("")));
    }


    private void submitForm() {
        if (!validateName()) {
            return;
        }



        if (!validatePassword()) {
            return;
        }
        EditText etun = (EditText) findViewById(R.id.input_name);
        EditText etpass = (EditText) findViewById(R.id.input_password);
        //If user wants to continue being logged in
        User user = new User(etun.getText().toString(), etpass.getText().toString());
        //Select corresponding user from the database after passing username(ONLINE DB)
        serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDatafromBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser != null)
                {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(login.this, MapsActivity.class);
                    userlocalstore.storePersonal(returnedUser);

                    startActivity(intent);
                }

            }
        });


    }



    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            //     err_msg_password = (TextView) findViewById(R.id.err_msg_name);
            inputLayoutName.setError((Html.fromHtml("<font color='#FFFFFF'>Enter Username</font>")));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }



    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(Html.fromHtml("<font color='#FFFFFF'>Enter password</font>"));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        if (count == 1) {
            count = 0;
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            Toast.makeText(getApplicationContext(), "Press again to quit.", Toast.LENGTH_SHORT).show();
            count++;
        }

        return;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }



}
