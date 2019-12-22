package com.example.pengout.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pengout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings, logoutButton;
    private EditText name, email, age, address;
    private String[] genderSelection = {"Male", "Female","Other"};
    private Spinner genderSpinner;
    private CircleImageView userProfileImage;


    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private static final int GALLERYPICK = 1;
    private StorageReference userProfileImagesRef;

    private ProgressDialog loadingBar;

    String spinVal;

    private Toolbar settingsToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        initializeFields();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERYPICK);
            }
        });

//        userName.setVisibility(View.INVISIBLE);

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });


        retrieveUserInfo();
    }


    private void initializeFields() {

        genderSpinner = findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinVal = genderSelection[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(SettingsActivity.this, R.layout.support_simple_spinner_dropdown_item, genderSelection);
        genderSpinner.setAdapter(spinAdapter);

        updateAccountSettings = findViewById(R.id.update_settings_button);
        logoutButton = findViewById(R.id.logout_settings_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingsActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });

        name = findViewById(R.id.my_name_edittext);
        email = findViewById(R.id.my_email_edittext);
        age = findViewById(R.id.my_age_edittext);
        address = findViewById(R.id.my_address_edittext);

        userProfileImage = findViewById(R.id.set_profile_image);

        loadingBar = new ProgressDialog(this);

        settingsToolBar = findViewById(R.id.my_profile_settings_toolbar);
        setSupportActionBar(settingsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Settings");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                loadingBar.setTitle("Set Profile Image");
                loadingBar.setMessage("Please wait, your profile image is uploading...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();
                final StorageReference filePath = userProfileImagesRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();


                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    rootRef.child("users").child(currentUserID).child("image")
                                            .setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SettingsActivity.this, "Image save in Database, Successfully...", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                    else {
                                                        String message = task.getException().toString();
                                                        Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });


                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }

    private void updateSettings() {

        String setName = name.getText().toString();
        String setEmail = email.getText().toString();
        int setAge = Integer.parseInt(age.getText().toString());
        String setAddress = address.getText().toString();
        String setGender = genderSpinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(setName)) {
            Toast.makeText(this, "Please enter your name first...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEmail)) {
            Toast.makeText(this, "Please enter your email...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setAddress)) {
            Toast.makeText(this, "Please enter your address...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setGender)) {
            Toast.makeText(this, "Please select your gender...", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setName);
            profileMap.put("email", setEmail);
            profileMap.put("age", setAge);
            profileMap.put("address", setAddress);
            profileMap.put("gender", setGender);

            rootRef.child("users").child(currentUserID).updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                sendUserToMyProfileActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    private void sendUserToMyProfileActivity() {
        Intent chatIntent = new Intent(SettingsActivity.this, MyProfileActivity.class);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(chatIntent);
        finish();

    }

    private void retrieveUserInfo() {
        rootRef.child("users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))) {
//                        if ((dataSnapshot.exists() && (dataSnapshot.hasChild("image")))) {
                            String retrieveUserName = (String) dataSnapshot.child("name").getValue();
                            String retrieveStatus = (String) dataSnapshot.child("email").getValue();
                            String retrieveProfileImage = (String) dataSnapshot.child("image").getValue();
                            String retreiveAge = dataSnapshot.child("age").getValue().toString();
                            String retreiveGender = (String) dataSnapshot.child("gender").getValue();
                            String retrieveAddress = (String) dataSnapshot.child("address").getValue();

                            name.setText(retrieveUserName);
                            email.setText(retrieveStatus);
                            age.setText(retreiveAge);
                            genderSpinner.setPrompt(retreiveGender);
                            address.setText(retrieveAddress);
                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);
                        }
                        else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {
                            String retrieveUserName = (String) dataSnapshot.child("name").getValue();
                            String retrieveStatus = (String) dataSnapshot.child("email").getValue();
                            String retrieveAge = (String) dataSnapshot.child("age").getValue();
                            String retrieveGender = (String) dataSnapshot.child("gender").getValue();
                            String retrieveAddress = (String) dataSnapshot.child("address").getValue();

                            name.setText(retrieveUserName);
                            email.setText(retrieveStatus);
                            age.setText(retrieveAge);
                            genderSpinner.setSelection(Arrays.asList(genderSelection).indexOf(retrieveGender));
                            address.setText(retrieveAddress);
                        }
                        else {
                            name.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this, "Please set & update your profile information", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
