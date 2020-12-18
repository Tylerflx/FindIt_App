package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class JobInfo extends AppCompatActivity {
    private WebView myweb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);
        //passing id from Adapter.java
        String id = getIntent().getStringExtra("id");
        //modify url for each job id
        String url = "https://jobs.github.com/positions/" + id;
        myweb = (WebView) findViewById(R.id.webView);
        WebSettings web = myweb.getSettings();
        web.setJavaScriptEnabled(true);
        //load html url for webview
        myweb.loadUrl(url);
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
                        startActivity(new Intent(getApplicationContext(), ProfileView.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}