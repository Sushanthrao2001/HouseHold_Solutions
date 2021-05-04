package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class send_otp extends AppCompatActivity {
    public Button verify,resend;
    public EditText otp;
    public ProgressBar progressBar;
    public String VerificationCodeBySystem,number,email,password,address,name,Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        verify=findViewById(R.id.getbtn);
        otp=findViewById(R.id.mble);
        resend=findViewById(R.id.resendotpbtn);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        number=getIntent().getStringExtra("Phone");
        email=getIntent().getStringExtra("Email");
        password=getIntent().getStringExtra("Password");
        address=getIntent().getStringExtra("Address");
        name=getIntent().getStringExtra("Name");
        Type=getIntent().getStringExtra("type");

        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);

        send_verification_code_to_user(number);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otp.getText().toString();

                if(code.isEmpty() || code.length()<6){
                    otp.setError("Wrong OTP..");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_verification_code_to_user(number);
                Toast.makeText(send_otp.this, "OTP Resend Succesfully!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void send_verification_code_to_user(String number){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91"+number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);//Function Works if the mobile number is different

            VerificationCodeBySystem=s;//Code that system sent
            otp.setVisibility(View.VISIBLE);
            resend.setEnabled(false);
            Toast.makeText(send_otp.this, "OTP Has Been Sent To "+number+" .", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);//Automatic Code Verification
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(send_otp.this,"Wrong OTP !!", Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codebyuser){

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerificationCodeBySystem,codebyuser);
        signintheuserbycredentials(credential);

    }
    private void signintheuserbycredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(send_otp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.createUserWithEmailAndPassword(email,password)//Storing the data of user through email and password authentication
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                if (Type.equals("User")){
                                                    String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    dataholder_user obj=new dataholder_user(email,password,address,name,number,id);
                                                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                                                    DatabaseReference node=db.getReference("_Users");
                                                    DatabaseReference node1=node.child(id);
                                                    node1.setValue(obj);
                                                    Toast.makeText(send_otp.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show();
                                                }
                                                if (Type.equals("Employee")){
                                                    Toast.makeText(send_otp.this, "Registered Succesfully!", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(send_otp.this, "Login to get more details", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(send_otp.this,employee.class);
                                                    startActivity(intent);
                                                }
                                            }else{
                                                Toast.makeText(send_otp.this, "User Already Registered!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(send_otp.this, "User Already Registered!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent=new Intent(send_otp.this,login_signup.class);//Intent to call after completion
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(send_otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}