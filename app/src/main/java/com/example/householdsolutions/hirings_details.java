package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class hirings_details extends AppCompatActivity {
    public String name,method,address,customerid,date,district,occupation,status;
    public TextView name1,method1,address1,id1,date1;
    public Button update;
    private RadioButton rad1,rad2;
    public RadioButton radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirings_details);
        name1=findViewById(R.id.nameid);
        method1=findViewById(R.id.methodid);
        address1=findViewById(R.id.addid);
        id1=findViewById(R.id.custid);
        update=findViewById(R.id.update);
        date1=findViewById(R.id.dateid);
        rad1=findViewById(R.id.rd1);
        rad2=findViewById(R.id.rd2);

        name=getIntent().getStringExtra("Name");
        address=getIntent().getStringExtra("Address");
        method=getIntent().getStringExtra("Method");
        customerid=getIntent().getStringExtra("Id");
        date=getIntent().getStringExtra("Date");
        district=getIntent().getStringExtra("District");
        occupation=getIntent().getStringExtra("Occupation");
        status=getIntent().getStringExtra("Status");
        Toast.makeText(this,status, Toast.LENGTH_SHORT).show();
        if (status.equals("Complete")){
           rad1.setChecked(true);
        }
        name1.setText(name);
        id1.setText(customerid);
        address1.setText(address);
        date1.setText(date);
        method1.setText(method);

        DatabaseReference node= FirebaseDatabase.getInstance().getReference("_Bookings List");
        DatabaseReference node1=node.child(district).child(occupation);
        DatabaseReference node2=node1.child(name);
        Toast.makeText(this, node2.getKey(), Toast.LENGTH_SHORT).show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                node2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           Toast.makeText(hirings_details.this,"Found", Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(hirings_details.this, "Data Not Found!!", Toast.LENGTH_SHORT).show();
                       }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(hirings_details.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}