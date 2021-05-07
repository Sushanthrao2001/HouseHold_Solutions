package com.example.householdsolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class account_info extends AppCompatActivity {
    public TextView name,mail,number,id;
    private FirebaseAuth mAuth;
    private String mail_lgn;
    private Button btn;
    public AlertDialog.Builder mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        name=findViewById(R.id.namedb);
        mail=findViewById(R.id.emaildb);
        number=findViewById(R.id.mobiledb);
        id=findViewById(R.id.iddb);
        btn=findViewById(R.id.button);
        mydialog=new AlertDialog.Builder(account_info.this);

        mAuth = FirebaseAuth.getInstance();
        id.setText(mAuth.getCurrentUser().getUid());
        mail_lgn = mAuth.getCurrentUser().getEmail();

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference node=db.getReference("_Users");
        DatabaseReference node1=node.child(mAuth.getCurrentUser().getUid());
        node1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mAuth.getCurrentUser().getUid().equals(snapshot.child("id").getValue().toString())){
                    name.setText(snapshot.child("name").getValue().toString()+",");
                    mail.setText(snapshot.child("email").getValue().toString());
                    number.setText(snapshot.child("number").getValue().toString());
                }else{
                    Toast.makeText(account_info.this, "Mail ID is Not Matched!! Details May not be accurate", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(account_info.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
        //Code For Updating the firebase..
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.setTitle("Edit the Name:");

                final EditText editText=new EditText(account_info.this);
                mydialog.setView(editText);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(editText.getText().toString()+",");
                    }
                });
                mydialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mydialog.show();
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.setTitle("Edit the Mail:");

                final EditText editText=new EditText(account_info.this);
                mydialog.setView(editText);

                mydialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mail.setText(editText.getText().toString());
                    }
                });
                mydialog.show();
            }
        });
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(account_info.this, "Number Cannot be Updated as of security issues!", Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(account_info.this, "Click on the prefered text to get Updated!", Toast.LENGTH_SHORT).show();
                Toast.makeText(account_info.this, "Then Press Update to modify.", Toast.LENGTH_SHORT).show();

                node1.child("name").setValue(name.getText().toString().replace(",","").trim());
                node1.child("email").setValue(mail.getText().toString());
                node1.child("number").setValue(number.getText().toString());
                Toast.makeText(account_info.this, "Updated Succesfully!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
