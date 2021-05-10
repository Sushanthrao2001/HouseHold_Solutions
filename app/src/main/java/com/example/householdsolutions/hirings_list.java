package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.os.Build.ID;

public class hirings_list extends AppCompatActivity {
    private Spinner dists,occupation1;
    public String district,occupation;
    private Button btn;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirings_list);
        btn=findViewById(R.id.button);
        listView=findViewById(R.id.list);
        dists =(Spinner) findViewById(R.id.dist);
        ArrayAdapter<String> myadapter = new ArrayAdapter(hirings_list.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.districts));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dists.setAdapter(myadapter);
        occupation1=(Spinner)findViewById(R.id.occup);
        ArrayAdapter<String> adapter=new ArrayAdapter(hirings_list.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.occupations));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupation1.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                district=dists.getSelectedItem().toString();
                occupation=occupation1.getSelectedItem().toString();

                ArrayList<String> list1= new ArrayList<String>();
                ArrayAdapter adapter2= new ArrayAdapter(hirings_list.this,android.R.layout.simple_list_item_1,list1);
                listView.setAdapter(adapter2);

                DatabaseReference node= FirebaseDatabase.getInstance().getReference("_Bookings List");
                DatabaseReference node1=node.child(district).child(occupation);
                node1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list1.clear();
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                                for (DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                    String name=snapshot1.child("name").getValue().toString();
                                    String id=snapshot1.child("customerid").getValue().toString();
                                    String date=snapshot1.child("assigned_Date").getValue().toString();
                                    String method=snapshot1.child("paymentmethod").getValue().toString().replace("(Gpay,Phonepay,Bank Payment)"," ");
                                    String address=snapshot1.child("service_address").getValue().toString();
                                    String status=snapshot1.child("status").getValue().toString();
                                    String ex="Name: "+name+"\n       "+"CustomerID: "+id+"\n       "+"Assigned Date: "+date+"\n       "+"Address: "+address+"\n       "+"Method: "+method+"\n       "+"Status: "+status;
                                    list1.add(ex);
                                }
                            }
                        }else{
                            Toast.makeText(hirings_list.this, "No Hirings!!", Toast.LENGTH_SHORT).show();
                        }
                        adapter2.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(hirings_list.this, "Error Occcured!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=listView.getItemAtPosition(position).toString();
                String [] splittedarray=data.split("\n");
                String [] name1=splittedarray[0].split(":");
                String [] id1=splittedarray[1].split(":");
                String [] date1=splittedarray[2].split(":");
                String [] address1=splittedarray[3].split(":");
                String [] method1=splittedarray[4].split(":");
                String [] status1=splittedarray[5].split(":");
                Intent intent=new Intent(hirings_list.this,hirings_details.class);
                intent.putExtra("Name", name1[1].trim());
                intent.putExtra("Id",id1[1].trim());
                intent.putExtra("Date",date1[1].trim());
                intent.putExtra("Address",address1[1].trim());
                intent.putExtra("Method",method1[1].trim());
                intent.putExtra("Status",status1[1].trim());
                intent.putExtra("District",district);
                intent.putExtra("Occupation",occupation);
                startActivity(intent);
            }
        });
    }
}