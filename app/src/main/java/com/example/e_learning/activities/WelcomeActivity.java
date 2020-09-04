package com.example.e_learning.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_learning.R;
import com.example.e_learning.models.Users;
import com.example.e_learning.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity {
    private Button joinNow_button, login_button;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getDataWelcome();
        getIntentsWelcome();
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        //Don't forget, We've an error here...
        if (!Objects.equals(UserPhoneKey, "") && !Objects.equals(UserPasswordKey, "")){
            if (!TextUtils.isEmpty(UserPhoneKey)&&!TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already logged in");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String phoneNumber, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.child("Users").child(phoneNumber).exists())){
                    //we had an error here!
                    Users usersData = new Users();
                    usersData.setPhone(phoneNumber);
                    usersData.setPassword(password);
                    snapshot.child("Users").child(phoneNumber).getValue(Users.class);
                    if (usersData.getPhone().equals(phoneNumber)){
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(WelcomeActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(WelcomeActivity.this, BookingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(WelcomeActivity.this, "Account with this "+phoneNumber+" doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WelcomeActivity.this, "Something happened\n"+error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getIntentsWelcome() {
        joinNow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataWelcome(){
        joinNow_button = findViewById(R.id.join_now_button);
        login_button = findViewById(R.id.login_welcome_button);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);
    }
}