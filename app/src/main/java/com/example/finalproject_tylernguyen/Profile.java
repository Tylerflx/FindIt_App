package com.example.finalproject_tylernguyen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView signup;
    private Button login;
    private ImageView login_google;
    private EditText email_id, pass_id;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //declare variable
        email_id = (EditText) findViewById(R.id.login_email);
        pass_id = (EditText) findViewById(R.id.login_pwd);
        signup = (TextView) findViewById(R.id.signup_text);
        // if signup button on click, switch to signup page

        login = (Button) findViewById(R.id.login_btn);
        login_google = (ImageView) findViewById(R.id.google);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //token from gradle signin report
                .requestIdToken("317165485211-rsqso8qfhspshcv9jeqmqi0m43ume42s.apps.googleusercontent.com")
                .requestEmail()
                .build();
        //init signin client
        googleSignInClient = GoogleSignIn.getClient(Profile.this, gso);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //init firebase user
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //check condition
        if (firebaseUser != null){
            //when user already sign in
            //redirect to profileview
            startActivity(new Intent(Profile.this, ProfileView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        //google login onclick listener
        login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToast("Google button onclicked");
                Intent intent = googleSignInClient.getSignInIntent();
                //start activity for result
                startActivityForResult(intent, 100);
            }
        });
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
                }else if(email.isEmpty()){
                    //ask user to enter email
                    email_id.setError("Please enter your email");
                    email_id.requestFocus();

                }else if(pass.isEmpty()){
                    //ask user to enter pass
                    pass_id.setError("Please enter your password");
                    pass_id.requestFocus();
                }else{
                    //if everything filled out, check firebase for credential
                    try {
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Profile.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    displayToast("Log in Failed, try again");
                                } else {
                                    //if login success, switch to ProfileView activity
                                    Toast.makeText(Profile.this, "Yay!", Toast.LENGTH_SHORT).show();
                                    //Intent intent = new Intent(Profile.this, ProfileView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //intent.putExtra("")
                                    startActivity(new Intent(Profile.this, ProfileView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Signup.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    //check condition
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        //check credential
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //when task is successful
                                    //display toast
                                    //redirect to profileview
                                    displayToast("Google SignIn Successful");
                                    startActivity(new Intent(Profile.this, ProfileView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                } else {
                                    //when not successful
                                    //display toast
                                    displayToast("Authentication Failed" + task.getException().getMessage());
                                }
                            }
                        });
                    }

                } catch (ApiException e) {
                    displayToast("Not good");
                    e.printStackTrace();
                }
            }
            }
        }
    public void displayToast(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}