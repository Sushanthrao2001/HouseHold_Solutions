package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
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
    public String name,method,address,customerid,date,district,occupation,status,status1;
    public TextView name1,method1,address1,id1,date1;
    public Button update;
    private RadioButton rad1,rad2;

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

        name=getIntent().getStringExtra("Name").trim();
        address=getIntent().getStringExtra("Address").trim();
        method=getIntent().getStringExtra("Method").trim();
        customerid=getIntent().getStringExtra("Id").trim();
        date=getIntent().getStringExtra("Date").trim();
        district=getIntent().getStringExtra("District").trim();
        occupation=getIntent().getStringExtra("Occupation").trim();
        status=getIntent().getStringExtra("Status").trim();
        name1.setText(name);
        id1.setText(customerid);
        address1.setText(address);
        date1.setText(date);
        method1.setText(method);
        if (status.equals("Complete")){
            rad1.setChecked(true);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference node= FirebaseDatabase.getInstance().getReference("_Bookings List");
                DatabaseReference node1=node.child(district).child(occupation);
                DatabaseReference node2=node1.child(name).child(customerid);
                if (rad1.isChecked()){
                    status1=rad1.getText().toString();
                }
                if (rad2.isChecked()){
                    status1=rad2.getText().toString();
                }
                if (status.equals(status1)){
                    Toast.makeText(hirings_details.this, "No Changes Were Done!!", Toast.LENGTH_SHORT).show();
                }else {
                    node2.child("status").setValue(status1);
                    Toast.makeText(hirings_details.this, "Modified", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}