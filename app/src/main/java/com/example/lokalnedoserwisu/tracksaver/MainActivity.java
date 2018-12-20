package com.example.lokalnedoserwisu.tracksaver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button login;
    private Button register;

    private String userIdentificator;
    private String email;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                    , 10);
            return;
        }

        initViews();
        initListeners();
        initObjects();
//        databaseHelper.clearBase();

    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(MainActivity.this);
        inputValidation = new InputValidation(MainActivity.this);
    }

    private void initViews(){
        login=findViewById(R.id.login_button);
        register=findViewById(R.id.register_button);

        emailEditText=findViewById(R.id.email_login_id);
        passwordEditText=findViewById(R.id.login_password_id);
    }

    private void initListeners(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdentificator=emailEditText.getText().toString().trim();
                password=passwordEditText.getText().toString().trim();
                if(!userIdentificator.isEmpty()&&
                        !password.isEmpty()){
                    Boolean bool = ((databaseHelper.checkUserByName(userIdentificator) || databaseHelper.checkUserByEmail(userIdentificator)));
                    if (databaseHelper.checkUserByName(userIdentificator) || databaseHelper.checkUserByEmail(userIdentificator)){
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(userIdentificator).matches()) {
                            username = databaseHelper.getUserByEmail(userIdentificator).getName();
                            email = userIdentificator;
                        }
                        else {
                            username = userIdentificator;
                            email = databaseHelper.getUserByName(userIdentificator).getEmail();
                        }

                        if (databaseHelper.checkUser(email, password)){
                            getApplication().getSharedPreferences("dane", Context.MODE_PRIVATE).edit().putString("username", username).commit();
                            getApplication().getSharedPreferences("dane", Context.MODE_PRIVATE).edit().putString("email", email).commit();
                            Intent intent;
                            intent= new Intent(getApplicationContext(), TracksActivity.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        Snackbar.make(findViewById(R.id.main_activity), "Invalid email or password\nTry again or register", Snackbar.LENGTH_LONG).show();
                    }
                }
                else {
                    Snackbar.make(findViewById(R.id.main_activity), "Fields email and password can not be empty\nTry again", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
