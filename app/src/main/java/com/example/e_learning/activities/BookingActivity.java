package com.example.e_learning.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_learning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Book;

public class BookingActivity extends AppCompatActivity {
    private EditText fullName_editText, phoneNumber_editText, parentsNumber_editText;
    private String parentDBName = "Data";
    private ProgressDialog loadingBar;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        fullName_editText = findViewById(R.id.full_name_booking);
        phoneNumber_editText = findViewById(R.id.phone_number_student_booking);
        parentsNumber_editText = findViewById(R.id.phone_number_parents_booking);
        loadingBar = new ProgressDialog(this);
        sendButton = findViewById(R.id.send_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadDataToServer();
            }
        });
    }

    private void UploadDataToServer() {
        String fullName = fullName_editText.getText().toString().trim();
        String phoneNumberStudent = phoneNumber_editText.getText().toString().trim();
        String phoneNumberParents = parentsNumber_editText.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Please, Write your Full Name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneNumberStudent)){
            Toast.makeText(this, "Please, Write your Phone number... ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneNumberParents)){
            Toast.makeText(this, "Please, Write your Parent's number...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Uploading Data");
            loadingBar.setMessage("Please wait, While we're Uploading your data");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateData(fullName, phoneNumberStudent, phoneNumberParents);
        }
    }

    private void ValidateData(final String fullName, final String phoneNumberStudent, final String phoneNumberParents) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child(parentDBName).child(phoneNumberStudent).exists())){
                    HashMap<String, Object> UsersData = new HashMap<>();
                    UsersData.put("Full name", fullName);
                    UsersData.put("Phone number", phoneNumberStudent);
                    UsersData.put("Parent's number", phoneNumberParents);

                    RootRef.child(parentDBName).child(phoneNumberStudent).updateChildren(UsersData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(BookingActivity.this, "Your Data has been Uploaded, Thank You!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(BookingActivity.this, "Error: please try again later...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(BookingActivity.this, "Error: please try again later...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingActivity.this, "Something happened\n"+error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}