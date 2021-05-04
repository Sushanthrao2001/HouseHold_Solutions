 package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

 public class details_preview extends AppCompatActivity {

    public static final String Name = "Name";
    public static final String Mobile="Mobile";
    //public static final String Email="Email";
    //public static final String Exper="Exper";
    public static final String Address="Address";
    public static final String District="District";
    public static final String Method="Method";
    public static final String Occupation="Occupation";
    private Button hire,call;
    public TextView name,mblenum,email,exp;
    public FirebaseAuth mauth;
    public String name1,method1,customerid,address1,district1,mblenum1,occupation1,namedb,SMS;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_preview);
        hire=findViewById(R.id.hirebtn);
        name=findViewById(R.id.nameid);
        //exp=findViewById(R.id.expid);
        mblenum=findViewById(R.id.numid);
        //email=findViewById(R.id.mailid);
        call=findViewById(R.id.callbtn);
        mauth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        name1=intent.getStringExtra("Name");
        mblenum1=intent.getStringExtra("Mobile");
        //email1=intent.getStringExtra("Email");
        //expe1=intent.getStringExtra("Exper");
        district1=intent.getStringExtra("District");
        address1=intent.getStringExtra("Address");
        method1=intent.getStringExtra("Method");
        occupation1=intent.getStringExtra("Occupation");
        name.setText(name1);
        mblenum.setText(mblenum1);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call=new Intent(Intent.ACTION_CALL);
                String number= mblenum.getText().toString();

                if (number.isEmpty()){
                    Toast.makeText(details_preview.this, "Sorry For The Incovience, Mobile Number Is Not Registered", Toast.LENGTH_SHORT).show();
                }
                else {
                    call.setData(Uri.parse("tel:"+number));
                }
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(details_preview.this, "Please Give The Permission To Call!", Toast.LENGTH_SHORT).show();
                    requestpermission(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
                else{
                    startActivity(call);
                }
            }
        });
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db=FirebaseDatabase.getInstance();
                DatabaseReference node=db.getReference("_Bookings List");

                if (mauth.getCurrentUser() != null)
                    customerid=mauth.getCurrentUser().getUid();
                else
                    Toast.makeText(details_preview.this, "Login To Continue!", Toast.LENGTH_SHORT).show();

                //Code To Send The SMS to the employee..
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

                    if (ActivityCompat.checkSelfPermission(details_preview.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                        sendsms();
                    else {
                        requestPermissions(new String[] {Manifest.permission.SEND_SMS},1);
                    }
                }

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                dataholder_order obj=new dataholder_order(address1,method1,currentDate,customerid);

                DatabaseReference node1=node.child(district1);
                DatabaseReference node2=node1.child(occupation1).child(name1);
                node2.child(customerid).setValue(obj);
                Toast.makeText(details_preview.this, "Hired Successfully.", Toast.LENGTH_SHORT).show();
                Toast.makeText(details_preview.this, " For Better Service You Can Contact"+name1, Toast.LENGTH_SHORT).show();
            }
            public void sendsms(){
                if (mauth.getCurrentUser() != null){
                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                    DatabaseReference node= db.getReference("_Users");
                    DatabaseReference node1=node.child(customerid);
                    node1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (customerid.equals(snapshot.child("id").getValue().toString())) {
                                namedb = snapshot.child("name").getValue().toString();
                                SMS = "Hy! I Am " + namedb + " the User Of House Hold Solutions App Of User Id " + customerid + " Wants Your Service At The Address " + address1 + ".";
                            } else {
                                Toast.makeText(details_preview.this, " Customer Id Not Matched! Please Relogin! ", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(mblenum1, null, SMS, null, null);
                                Toast.makeText(details_preview.this, "Message is Sent", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(details_preview.this, "Failed to Send Message", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(details_preview.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(details_preview.this, "Please Relogin! As your id is not validated!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void requestpermission(String[] strings, int i){
        ActivityCompat.requestPermissions(details_preview.this,new String[] {Manifest.permission.CALL_PHONE},1);
    }
}