package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Technician_activity extends AppCompatActivity {

    private Spinner spinner;
    public EditText address1;
    private Button btn1;
    private RadioButton rd1,rd2;
    private ListView listView;
    public String address,district,mblenum,expe,email,name,method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_activity);
        Spinner spinner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> myadapter=new ArrayAdapter<>(Technician_activity.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.districts));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myadapter);
        spinner.setPrompt("Please Select District");
        btn1=findViewById(R.id.btn);
        address1=findViewById(R.id.add1);
        rd1=findViewById(R.id.rd1);
        rd2=findViewById(R.id.rd2);
        listView=findViewById(R.id.listviewid);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address=address1.getText().toString();
                district=spinner.getSelectedItem().toString();
                if (spinner.isSelected()) {
                    Toast.makeText(Technician_activity.this, "Please Select District!", Toast.LENGTH_SHORT).show();
                }else if (address.isEmpty()) {
                    Toast.makeText(Technician_activity.this, "Enter Your Address!", Toast.LENGTH_SHORT).show();
                }else if (rd1.isChecked() || rd2.isChecked()){
                    ArrayList<String> list= new ArrayList<String>();
                    ArrayAdapter adapter= new ArrayAdapter(Technician_activity.this,android.R.layout.simple_list_item_1,list);
                    listView.setAdapter(adapter);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
                    DatabaseReference reference1=reference.child(district).child("AC Technician");
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                name=snapshot.child("name").getValue().toString();
                                mblenum=snapshot.child("mobileNumber").getValue().toString();
                                expe=snapshot.child("experience").getValue().toString();
                                email=snapshot.child("mailid").getValue().toString();
                                String x= "Name: " +name+"  -->  "+" Mobile: "+mblenum;
                                list.add(x);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Technician_activity.this, "Service is not available in your area", Toast.LENGTH_SHORT).show();
                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String item=(String)listView.getItemAtPosition(position).toString();
                            String [] splittedarray=item.split("  -->  ");
                            String []Name= splittedarray[0].split(":");
                            String []Mobile= splittedarray[1].split(":");

                            if (rd1.isChecked()){
                                method=rd1.getText().toString();
                            }else {
                                method=rd2.getText().toString();
                            }
                            Intent intent= new Intent(Technician_activity.this,details_preview.class);
                            intent.putExtra(details_preview.Name,Name[1]);
                            intent.putExtra(details_preview.Mobile,Mobile[1]);
                            intent.putExtra(details_preview.District,district);
                            intent.putExtra(details_preview.Address,address);
                            intent.putExtra(details_preview.Method,method);
                            intent.putExtra(details_preview.Occupation,"AC Technician");
                            startActivity(intent);
                        }
                    });
                }else {
                    Toast.makeText(Technician_activity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}