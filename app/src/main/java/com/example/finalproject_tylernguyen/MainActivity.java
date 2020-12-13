package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //declare varibles
    private EditText title_id, location_id;
    private Button search_btn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set variables
        title_id = (EditText) findViewById(R.id.job_title);
        location_id = (EditText) findViewById(R.id.location);
        search_btn = (Button) findViewById(R.id.findit);

        //if search button on click
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user inputs set to string
                String title = title_id.getText().toString();
                String location = location_id.getText().toString();
                //call api

            }
        });





        //init
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Select
        bottomNavigationView.setSelectedItemId(R.id.home);
        //Perform Item Selected List
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.fav:
                        startActivity(new Intent(getApplicationContext(), Favourite.class));
                        overridePendingTransition(0,0);
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