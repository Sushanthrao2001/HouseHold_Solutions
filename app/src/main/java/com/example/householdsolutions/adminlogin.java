package com.example.householdsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class adminlogin extends AppCompatActivity {
    public Button login;
    public EditText email, password;
    public String memail, mpwd;
    public FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        login = findViewById(R.id.lgn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pswd);
        mauth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });
    }

    private void loginuser() {

        memail = email.getText().toString();
        mpwd = password.getText().toString();
        if (memail.isEmpty()) {
            email.setError("Please Fill The Email Field!!");
        }
        if (mpwd.isEmpty()) {
            password.setError("Please Fill The Password!!");
        }
        if (!memail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
            if (!mpwd.isEmpty()) {
                if ((memail.equals("admin@gmail.com"))&&(mpwd.equals("admin@123"))){
                    Toast.makeText(adminlogin.this, "Login Successfully!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(adminlogin.this,hirings_list.class));
                    finish();
                }else{
                    Toast.makeText(this, "The Credentials Were Not Matched!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}