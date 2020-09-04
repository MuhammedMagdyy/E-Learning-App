package com.example.e_learning.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;
import com.example.e_learning.R;
import com.example.e_learning.adapters.DataShowAdapter;
import com.example.e_learning.models.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DataShowAdapter dataShowAdapter;
    ArrayList<Data> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        recyclerView = findViewById(R.id.list_item1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataShowAdapter = new DataShowAdapter();
        recyclerView.setAdapter(dataShowAdapter);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference scoreRef = rootRef.child("Data");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String Full_name = dataSnapshot.child("Full name").getValue(String.class);
                    String Phone_student = dataSnapshot.child("Phone number").getValue(String.class);
                    String Phone_parents = dataSnapshot.child("Parent's number").getValue(String.class);
                    list.add(new Data("Full name: "+Full_name, "Student number: "+Phone_student, "Parent number: "+Phone_parents));
                }
                dataShowAdapter.setmData(list);
                recyclerView.setAdapter(dataShowAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);

    }

}