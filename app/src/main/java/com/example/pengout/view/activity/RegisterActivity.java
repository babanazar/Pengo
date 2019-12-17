package com.example.pengout.view.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;


    private Boolean isLogin = false;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef, usersRef;

    FirebaseUser firebaseUser;

    TextView already;
    Button buttonSignup;

    @Override
    protected void onStart() {
        super.onStart();

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Log.d("Firebasejan", "FirebaseUser is null");
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        //initializing views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        already = findViewById(R.id.reg_text);
        buttonSignup = findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        already.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

//                TextView already = findViewById(R.id.reg_text);
                if (!isLogin) {

                    already.setText("First time? Welcome!");
                    buttonSignup.setText("Login");
                    isLogin = true;
//                    return false;
                }
                else {

                    already.setText("Already have an account?");
                    buttonSignup.setText("Register");
                    isLogin = false;
//                    return false;
                }
                return true;
            }
        });
    }


    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length() < 6){
            Toast.makeText(this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            return;

        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String deviceToken = FirebaseInstanceId.getInstance().getToken();


                            String currentUserID = firebaseAuth.getCurrentUser().getUid();
                            rootRef.child("users").child(currentUserID).setValue("");

                            rootRef.child("users").child(currentUserID).child("device_token")
                                    .setValue(deviceToken);

                            Toast.makeText(RegisterActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, InformationActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                            progressDialog.dismiss();
                            return;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createUserWithEmail", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }


    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Logging in Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String currentUserID = firebaseAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            usersRef.child(currentUserID).child("device_token")
                                    .setValue(deviceToken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                progressDialog.dismiss();

                                            }
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithEmail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        //calling register method on click
        if(!isLogin){
            registerUser();
        }
        if(isLogin){
            loginUser();
        }

    }


}