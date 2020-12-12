package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileView extends AppCompatActivity {
    TextView tvName;
    ImageView ivImage;
    Button logout_btn;
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //Assign variable
        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.profile_view);
        logout_btn = findViewById(R.id.logout_btn);

        //Init firebase auth
        mAuth = FirebaseAuth.getInstance();
        //Init firebase user
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //check condition
        if (firebaseUser != null){
            //when firebaseuser is not null
            //set imageview
            if (firebaseUser.getDisplayName() != null){
                //if user login with google and be able to display name
                Glide.with(ProfileView.this).load(firebaseUser.getPhotoUrl()).into(ivImage);
                //set name on text view
                tvName.setText(firebaseUser.getDisplayName());
            }else{
                //set any user image to default image
                ivImage.setImageResource(R.mipmap.ic_avatar);
                //if not then display user email
                tvName.setText(firebaseUser.getEmail());
            }


        }

        //init sign in client
        googleSignInClient = GoogleSignIn.getClient(ProfileView.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        //set btn on click
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sign out from Google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //When task is successful
                            //Signout
                            mAuth.signOut();
                            //Display Toast
                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                            //Finish activity
                            finish();
                        }
                    }
                });
            }
        });
        //init bottom view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Select
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
}