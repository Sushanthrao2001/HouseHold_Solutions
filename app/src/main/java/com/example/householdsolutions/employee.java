package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class employee extends AppCompatActivity {
    private Button lgn,admin;
    private EditText memail,mpwd;
    private TextView textView;
    private FirebaseAuth mauth;
    public String email,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        memail=findViewById(R.id.emailid);
        mpwd=findViewById(R.id.pswd);
        textView=findViewById(R.id.signup);
        lgn=findViewById(R.id.emplogin);
        admin=findViewById(R.id.admin);

        mauth = FirebaseAuth.getInstance();
        //DatabaseReference ref= FirebaseDatabase.getInstance().getReference("_Users");
        //ref.child();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(employee.this,register_user.class);
                intent.putExtra("Type","Employee");
                startActivity(intent);
            }
        });
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    registeremployee();
            }
            private void registeremployee() {
                email = memail.getText().toString();
                pwd = mpwd.getText().toString();
                if (email.isEmpty()) {
                    memail.setError("Please Fill The Email Field!!");
                }
                if (pwd.isEmpty()) {
                    mpwd.setError("Please Fill The Password!!");
                }
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pwd.isEmpty()) {
                        mauth.signInWithEmailAndPassword(email, pwd)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Intent intent=new Intent(employee.this,employee_register.class);
                                        intent.putExtra("Email",email);
                                        intent.putExtra("Password",pwd);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(employee.this, "The Credentials Were Not Matched!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(employee.this,adminlogin.class);
                startActivity(intent);
            }
        });
    }
}