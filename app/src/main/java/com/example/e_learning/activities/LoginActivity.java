package com.example.e_learning.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    private EditText phone_login, password_login;
    private Button login_now_button;
    private ProgressDialog loadingBar;
    private TextView signUp_textView, loginAsAdmin_textView, or_text_view;
    private CheckBox checkBox_login;
    private String parentDBName = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getDataLogin();
        login_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });
        getIntentsLogin();
    }

    private void getIntentsLogin() {


        signUp_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginAsAdmin_textView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                login_now_button.setText("Login Admin");
                loginAsAdmin_textView.setVisibility(View.INVISIBLE);
                or_text_view.setVisibility(View.INVISIBLE);
                signUp_textView.setVisibility(View.INVISIBLE);
                parentDBName = "Admins";
            }
        });
    }

    private void getDataLogin() {
        phone_login = findViewById(R.id.phone_number_login);
        password_login = findViewById(R.id.password_login);
        login_now_button = findViewById(R.id.login_button);
        loadingBar = new ProgressDialog(this);
        checkBox_login = findViewById(R.id.checkbox_login);
        Paper.init(this);
        signUp_textView = findViewById(R.id.sign_up_now_text_view);
        loginAsAdmin_textView = findViewById(R.id.login_admin);
        or_text_view = findViewById(R.id.or_text_view);
    }

    private void UserLogin() {
        String phoneNumber = phone_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please, Write your Phone Number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please, Write your Password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, While we're checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phoneNumber, password);
        }
    }

    private void AllowAccessToAccount(final String phoneNumber, final String password) {
        if (checkBox_login.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phoneNumber);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.child(parentDBName).child(phoneNumber).exists())){
                    //we had an error here!
                    Users usersData = new Users();
                    usersData.setPhone(phoneNumber);
                    usersData.setPassword(password);
                    snapshot.child(parentDBName).child(phoneNumber).getValue(Users.class);
                        if (usersData.getPhone().equals(phoneNumber)){
                            if (usersData.getPassword().equals(password)) {
                                if (parentDBName.equals("Admins")){
                                    Toast.makeText(LoginActivity.this, "Welcome Mr.Alaa, You're Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, AdminAddNewProductActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if (parentDBName.equals("Users")){
                                    Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, BookingActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this "+phoneNumber+" doesn't exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Please, login with a correct Phone Number...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something happened\n"+error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}