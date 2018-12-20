package com.example.lokalnedoserwisu.tracksaver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passowrdEditText;
    private EditText confirmPasswordEditText;

    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    private Button registerButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initObjects();
        initViews();
        initListeners();


    }

    private void initListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                password = passowrdEditText.getText().toString().trim();
                confirmPassword = confirmPasswordEditText.getText().toString().trim();
                if (!name.isEmpty() &&
                        !email.isEmpty() &&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                        !password.isEmpty() &&
                        password.equals(confirmPassword)) {
                    if (!databaseHelper.checkUserByEmail(email)) {
                        user.setEmail(email);
                        user.setName(name);
                        user.setPassword(password);
                        databaseHelper.addUser(user);
                        Intent intent;
                        intent= new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Snackbar.make(findViewById(R.id.register_layout), "User " + name + " created", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        Snackbar.make(findViewById(R.id.register_layout), "User with email: " + email + " allready exists", Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                        Snackbar.make(findViewById(R.id.register_layout), "Something went wrong\nUser " + name + " can not be created", Snackbar.LENGTH_SHORT).show();
                }
            }


        });
    }
    private void initViews() {
        nameEditText=findViewById(R.id.register_name);
        emailEditText=findViewById(R.id.register_emaail);
        passowrdEditText=findViewById(R.id.register_password);
        confirmPasswordEditText=findViewById(R.id.register_confirm_password);

        registerButton=findViewById(R.id.register_user_button);
    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        inputValidation = new InputValidation(RegisterActivity.this);
        user=new User();
    }
}
