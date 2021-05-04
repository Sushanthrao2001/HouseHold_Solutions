package com.example.householdsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class employee_register extends AppCompatActivity {

    private EditText name1,mobile1,exp1,dist1,occu,address1;
    private Button addbtn;
    public String mailid,password,custid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);
        name1=findViewById(R.id.nameid);
        mobile1=findViewById(R.id.mbleid);
        exp1=findViewById(R.id.expid);
        occu=findViewById(R.id.occid);
        dist1=findViewById(R.id.distid);
        addbtn=findViewById(R.id.btnid);
        address1=findViewById(R.id.add1);

        mailid=getIntent().getStringExtra("Email");
        password=getIntent().getStringExtra("Password");

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduser();
            }

            private void adduser() {
                String district =dist1.getText().toString();
                String occupation=occu.getText().toString();
                String name=name1.getText().toString();
                String mobileNumber=mobile1.getText().toString();
                String experience=exp1.getText().toString();
                String address=address1.getText().toString();

                FirebaseAuth mauth=FirebaseAuth.getInstance();
                custid=mauth.getCurrentUser().getUid();

                dataholder obj=new dataholder(name,custid,mailid,mobileNumber,password,experience,address);
                FirebaseDatabase db=FirebaseDatabase.getInstance();
                DatabaseReference node=db.getReference("Employees");
                DatabaseReference node1=node.child(district);
                DatabaseReference node2=node1.child(occupation);
                node2.child(name).setValue(obj);
                name1.setText("");
                mobile1.setText("");
                occu.setText("");
                exp1.setText("");
                dist1.setText("");
                Toast.makeText(employee_register.this, "Succesfull Registered", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(employee_register.this,employee.class);
                startActivity(intent);
            }
        });

    }
}