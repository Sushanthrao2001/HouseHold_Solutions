 package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_signup extends AppCompatActivity {

    private EditText memail, mpwd;
    private FirebaseAuth mauth;
    private TextView mtextview,forgetlink;
    private Button signinbtn,employeebtn;
    public AlertDialog.Builder mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup2);
        memail = findViewById(R.id.emailreg);
        mpwd = findViewById(R.id.pswdreg);
        mtextview = findViewById(R.id.textView);
        signinbtn = findViewById(R.id.loginbtn);
        forgetlink=findViewById(R.id.forget);
        employeebtn=findViewById(R.id.admnbtn);
        mydialog=new AlertDialog.Builder(login_signup.this);

        mauth = FirebaseAuth.getInstance();

        mtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login_signup.this, register_user.class);
                intent.putExtra("Type","User");
                startActivity(intent);
            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });
        forgetlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(login_signup.this, "Please Fill The Email Field", Toast.LENGTH_SHORT).show();
                }else{
                    mauth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(login_signup.this, "Reset Link Has Sent To Your Mail!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login_signup.this, "Error!The Email Is Not Registered!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
                employeebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(login_signup.this, employee.class));
                    }
                });
    }

    private void loginuser() {
        String email = memail.getText().toString();
        String pwd = mpwd.getText().toString();
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
                                    Toast.makeText(login_signup.this, "Login Successfully!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(login_signup.this, home.class));
                                    finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_signup.this, "The Credentials Were Not Matched!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    //Code For Auto Login
    @Override
    protected void onStart() {
        super.onStart();
       if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(login_signup.this,home.class));
            finish();
        }
    }
}