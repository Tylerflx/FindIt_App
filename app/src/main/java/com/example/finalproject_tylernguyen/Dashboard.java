package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Job> jobs;
    private static String URL = "";
    Adapter adapter;
    ImageView imageView;
    Job job;
    TextView search_title;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference,fvrtref,fvrt_listRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get current user uid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String currentUserid = user.getUid();
            databaseReference = database.getReference("Users");
            fvrt_listRef = database.getReference("FavoriteList").child(currentUserid);
        }

        //set up recyclerview
        recyclerView = findViewById(R.id.result_view);
        //declare new array list
        jobs = new ArrayList<>();
        search_title = (TextView) findViewById(R.id.jobTitle_box);
        if(getIntent().getExtras() != null){
            //if the data passed from the main activity is not null
            //then get that data and assign to variables
            String inp_job = getIntent().getStringExtra("jobTitle");
            String inp_loc = getIntent().getStringExtra("jobLoc");
            Toast.makeText(Dashboard.this,inp_job + inp_loc, Toast.LENGTH_SHORT).show();
            //specify the url
            if (inp_loc != null){
                //if job tittle and job location is available
                //search with job title and location
                String combine_str = inp_job + " " + inp_loc;
                search_title.setText("Result for "+combine_str);
                URL = "https://jobs.github.com/positions.json?description="+inp_job+ "&location="+inp_loc;
            }else{
                //if only tittle got passed
                //with button clicked on current location
                //set url link for lat and long
                String lati = getIntent().getStringExtra("lat_tude");
                String longi = getIntent().getStringExtra("lon_gitude");
                String combine_str = inp_job + "[" + lati + "," + longi +"]";
                search_title.setText("Result for " + combine_str);
                //if user click on current location
                URL = "https://jobs.github.com/positions.json?description="+inp_job+"?lat="+lati+"long="+longi;
                Toast.makeText(Dashboard.this, lati + longi, Toast.LENGTH_SHORT).show();

            }
        }else{
            //if data are not passed or modify
            //show all the results
            URL = "https://jobs.github.com/positions.json";
            search_title.setText("Result for All Jobs");
        }
        extractJobs(URL);
        //init bottom view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Select
        bottomNavigationView.setSelectedItemId(R.id.result);
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
                        startActivity(new Intent(getApplicationContext(), Favourite.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.result:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //after generate jobs

    }
    //call API for jobs
    private void extractJobs(String url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        //show loading
        Loading loading = new Loading(Dashboard.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        //start loading dialog
        loading.startLoading();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //show loading while retrieve data
                //once get response start do some data
                //iterate through data
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jobObject = response.getJSONObject(i);
                        //extract data
                        Job job = new Job();
                        job.setJob_id(jobObject.getString("id").toString());
                        job.setJob_url(jobObject.getString("url").toString());
                        job.setJob_title(jobObject.getString("title").toString());
                        job.setPosition_type(jobObject.getString("type").toString());
                        job.setJob_location(jobObject.getString("location").toString());
                        job.setLogo_url(jobObject.getString("company_logo").toString());
                        jobs.add(job);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //after retrieving data
                //dismiss loading dialog
                loading.dismiss();
                //set recyclerview
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //call adapter class
                adapter = new Adapter(Dashboard.this, jobs);
                //set the adapter to recyclerview
                recyclerView.setAdapter(adapter);
                //getjob(jobs);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);

    }
}