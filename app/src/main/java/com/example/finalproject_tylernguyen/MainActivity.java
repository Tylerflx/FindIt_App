package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {
    //declare varibles
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private EditText title_id, location_id;
    private Button search_btn;
    private FirebaseAuth mAuth;
    private ImageView current;
    GPSTracker gps;
    Double lat, lon;
    String bestProvider;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set variables
        title_id = (EditText) findViewById(R.id.job_title);
        location_id = (EditText) findViewById(R.id.location);
        search_btn = (Button) findViewById(R.id.findit);
        current = (ImageView) findViewById(R.id.imageView9);

        //if search button on click
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user inputs set to string
                String title = title_id.getText().toString();
                String location = location_id.getText().toString();
                //check if all the boxes are filled
                if (title.isEmpty() && location.isEmpty()) {
                    //if both boxes are empty
                    //prompt user fields are empty
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (title.isEmpty()) {
                    //if job title is empty then set the box error with text
                    title_id.setError("Please enter a job title");
                    title_id.requestFocus();
                } else if (location.isEmpty()) {
                    //if location is empty,set error
                    location_id.setError("Please enter a location");
                    location_id.requestFocus();
                } else {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.putExtra("jobTitle", title);
                    intent.putExtra("jobLoc", location);
                    startActivity(intent);
                }
            }
        });
        //if current button on click
        //check title
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user inputs set to string
                String title = title_id.getText().toString();
                if (title.isEmpty()) {
                    //if job title is empty then set the box error with text
                    title_id.setError("Please enter a job title");
                    title_id.requestFocus();
                } else {
                    //if title is filled
                    //call current location
                    current_loc(v, title);
                }
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
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.fav:
                        startActivity(new Intent(getApplicationContext(), Favourite.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.result:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    //call when click current location
    public void current_loc(View v, String title) {
        //init fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //init location
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            //init geocoder
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            //init address list
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            //get lat from the address list
                            lat = addresses.get(0).getLatitude();
                            //format it as 2 decimals
                            String s_lat = format("%.2f",lat);
                            //get lon from address list
                            lon = addresses.get(0).getLongitude();
                            //format
                            String s_lon = format("%.2f",lon);
                            //call dashboard activity
                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            //passing variable to display activity
                            intent.putExtra("jobTitle",title);
                            intent.putExtra("lat_tude", s_lat );
                            intent.putExtra("lon_gitude",s_lon);
                            //call
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }
}