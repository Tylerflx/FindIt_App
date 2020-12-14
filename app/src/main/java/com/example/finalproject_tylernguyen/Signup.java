package com.example.finalproject_tylernguyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private EditText emailId, passId, email2, pass2;
    private FirebaseAuth mAuth;
    private Button signup_btn;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //create new user
        //assign signup button
        signup_btn = findViewById(R.id.btn_cp);
        //assign variables
        emailId = findViewById(R.id.su_email);
        email2 = findViewById(R.id.su_email2);
        passId = findViewById(R.id.su_pass);
        pass2 = findViewById(R.id.su_pass2);
        progressBar = findViewById(R.id.progressbar_cp);

        mAuth = FirebaseAuth.getInstance();


        //regular onclick listener
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if login button onclick
                //check if user enter email and pass
                final String email = emailId.getText().toString();
                final String re_email = email2.getText().toString();
                final String pass = passId.getText().toString();
                final String re_pass = pass2.getText().toString();
                if (email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(Signup.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    //ask user to enter email
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();

                }else if(pass.isEmpty()){
                    //ask user to enter pass
                    passId.setError("Please enter your password");
                    passId.requestFocus();
                }else{
                    //if everything filled out, check if re-enter email match with entered email
                    // also passwords
                    if(!email.equals(re_email)) {
                        email2.setError("Email doesn't match!");
                        email2.requestFocus();
                    }else if(!pass.equals(re_pass)){
                        pass2.setError("Password doesn't match");
                        pass2.requestFocus();
                    }
                    if(email.equals(re_email) && pass.equals(re_pass)){
                        //if everything looks good
                        //create account
                        //set progress bar
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        displayToast("Signup Failed, try again");
                                    } else {
                                        //stop the progressbar
                                        progressBar.setVisibility(View.INVISIBLE);
                                        //if login success, switch to ProfileView activity
                                        Toast.makeText(Signup.this, "Account created!", Toast.LENGTH_SHORT).show();
                                        //delay a bit
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //post delayed
                                                startActivity(new Intent(Signup.this, Profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            }
                                        },2000);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
    }
    public void displayToast(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}