package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register_user extends AppCompatActivity {
    private EditText memail,mpwd,mble,address,name1;
    private Button signinbtn;
    private FirebaseAuth mauth;
    public String email,pwd,mnum,add,name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        memail=findViewById(R.id.emailreg);
        name1=findViewById(R.id.name);
        mpwd=findViewById(R.id.pswdreg);
        mble=findViewById(R.id.mblereg);
        address=findViewById(R.id.address);
        signinbtn=findViewById(R.id.signbtn);

        mauth = FirebaseAuth.getInstance();
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = memail.getText().toString();
                pwd = mpwd.getText().toString();
                mnum=mble.getText().toString();
                add=address.getText().toString();
                name2=name1.getText().toString();
                if (email.isEmpty()) {
                    memail.setError("Email Required!!");
                }
                if (pwd.isEmpty()) {
                    mpwd.setError("Password Required!!");
                }
                if (mnum.isEmpty()){
                    mble.setError("Mobile number Required");
                }
                if (mnum.length()<10){
                    mble.setError("Invalid Mobile Number");
                }
                if (add.isEmpty()){
                    address.setError("Address Required");
                }
                if (name2.isEmpty()){
                    name1.setError("Name Required");
                }
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if (!pwd.isEmpty() && mnum.length()==10){
                        Intent intent=new Intent(register_user.this,send_otp.class);
                        intent.putExtra("Phone",mnum);
                        intent.putExtra("Email",email);
                        intent.putExtra("Password",pwd);
                        intent.putExtra("Address",add);
                        intent.putExtra("Name",name2);
                        intent.putExtra("type",getIntent().getStringExtra("Type"));
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(register_user.this, "Please Enter Valid Email Id!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}