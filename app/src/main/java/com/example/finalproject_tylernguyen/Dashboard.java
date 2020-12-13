package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Job> jobs;
    private static String URL = "https://jobs.github.com/positions.json";
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //declare recyclerview
        recyclerView = findViewById(R.id.result_view);
        jobs = new ArrayList<>();
        extractJobs();

        //init
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
    }
    //call API for jobs
    private void extractJobs() {
        //show loading
        Loading loading = new Loading(Dashboard.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        //start loading dialog
        loading.startLoading();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
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
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new Adapter(getApplicationContext(), jobs);
                recyclerView.setAdapter(adapter);
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