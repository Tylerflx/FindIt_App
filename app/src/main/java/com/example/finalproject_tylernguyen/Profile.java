package com.example.finalproject_tylernguyen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView signup;
    private Button login;
    private ImageView login_google;
    private EditText email_id, pass_id;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //declare variable
        email_id = (EditText) findViewById(R.id.login_email);
        pass_id = (EditText) findViewById(R.id.login_pwd);
        signup = (TextView) findViewById(R.id.signup_text);
        login = (Button) findViewById(R.id.login_btn);
        login_google = (ImageView) findViewById(R.id.google);

        //regular onclick listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if login button onclick
                //check if user enter email and pass
                String email = email_id.getText().toString();
                String pass = pass_id.getText().toString();
                if (email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(Profile.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }else if(pass.isEmpty()){
                    //ask user to enter pass
                    pass_id.setError("Please enter your password");
                    pass_id.requestFocus();
                }else if(email.isEmpty()){
                    //ask user to enter email
                    email_id.setError("Please enter your email");
                    email_id.requestFocus();
                }else{
                    //if everything filled out, check firebase for credential
                    try {
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Profile.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Profile.this, "Log in Failed, try again", Toast.LENGTH_SHORT).show();
                                } else {
                                    //if login success, switch to ProfileView activity
                                    Toast.makeText(Profile.this, "Yay!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        //google login onclick listener
        login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Google button clicked", Toast.LENGTH_SHORT).show();
                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

            }
        });




        //check if user is currently signin
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
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
}