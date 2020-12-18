package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {
    //RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseUser user;
    List<Job> jobs;
    Adapter adapter;
    private RecyclerView fav_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        fav_list = findViewById(R.id.fav_view);
        ArrayList<String> list = new ArrayList<>();
        //fav_list = findViewById(R.id.result_view);
        jobs = new ArrayList<>();
        adapter = new Adapter(getApplicationContext(), jobs);
        fav_list.setAdapter(adapter);

        //firebase user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            //if user exist then display
            String currentUserId = user.getUid();
            reference = database.getReference().child("FavouriteList").child(currentUserId);
            //recall from database
            // Read from the database
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        list.add(snapshot.getValue().toString());
                    }
                    //adapter.notifyDataSetChanged();
                    //Toast.makeText(getApplicationContext(), "Added to Favourite List", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    System.out.println("The read failed: " + error.getCode());
                }
            });

        }
        //Toast.makeText(getApplicationContext(), "User uid: " + currentUserId, Toast.LENGTH_SHORT).show();



        //init
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Select
        bottomNavigationView.setSelectedItemId(R.id.fav);

        //Perform Item Selected List
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.fav:
                        return true;
                    case R.id.result:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}