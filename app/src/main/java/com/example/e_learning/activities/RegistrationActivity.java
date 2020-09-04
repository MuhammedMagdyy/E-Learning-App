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
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_learning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


public class RegistrationActivity extends AppCompatActivity {
    private EditText username_register, phoneNumber_register, password_register;
    private Button register_now_button;
    private TextView loginHere_textView;
    FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getDataRegister();
        register_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
        loginHere_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getDataRegister(){
        username_register = findViewById(R.id.username_registration);
        phoneNumber_register = findViewById(R.id.phone_number_registration);
        password_register = findViewById(R.id.password_registration);
        register_now_button = findViewById(R.id.registration_button);
        loginHere_textView = findViewById(R.id.login_now_text_view);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }
    private void CreateAccount() {
        String username = username_register.getText().toString().trim();
        String phone_number = phoneNumber_register.getText().toString().trim();
        String password = password_register.getText().toString().trim();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please, Write your Username...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone_number)){
            Toast.makeText(this, "Please, Write your Phone number... ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please, Write your Password...", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Creating your Account");
            loadingBar.setMessage("Please wait, While we're checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(username, phone_number, password);
        }
    }

    private void ValidatePhoneNumber(final String username, final String phone_number, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone_number).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Username", username);
                    userdataMap.put("Phone number", phone_number);
                    userdataMap.put("Password", password);

                    RootRef.child("Users").child(phone_number).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this, "Your Account has been created, Congratulations!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegistrationActivity.this, "Error: please try again later...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistrationActivity.this, "This "+phone_number+" already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegistrationActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrationActivity.this, "Something happened\n"+error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}