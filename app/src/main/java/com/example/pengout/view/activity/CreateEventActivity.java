package com.example.pengout.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pengout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateEventActivity extends AppCompatActivity {

    private Button createEventButton;
    private EditText eventTitle, eventCategory, eventLocation;
    private TextView eventTime;
    ImageView eventImage;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private static final int GALLERYPICK = 1;
    private StorageReference eventImageRef;

    private ProgressDialog loadingBar;

    private Toolbar settingsToolbar;

    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        eventImageRef = FirebaseStorage.getInstance().getReference().child("Event Images");

        initializeFields();

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERYPICK);
            }
        });
        final String timeOfEvent;
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTime.setText(showDateTimePicker());
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

    }




    private void initializeFields() {
        createEventButton = findViewById(R.id.create_event_button);

        eventTitle = findViewById(R.id.event_title);
        eventCategory = findViewById(R.id.event_category);
        eventTime = findViewById(R.id.event_time);
        eventLocation = findViewById(R.id.event_location);

        eventImage = findViewById(R.id.set_event_image);


        loadingBar = new ProgressDialog(this);
        settingsToolbar = findViewById(R.id.settings_toolbar);

        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Create Event");
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

            if (resultCode == RESULT_OK) {
                loadingBar.setTitle("Set Event Image");
                loadingBar.setMessage("Please wait, event image is uploading...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();
                String timestamp = getCurrentTimestamp();
                final StorageReference filePath = eventImageRef.child(currentUserID + "_" + timestamp);

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateEventActivity.this, "Event Image Is Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUri = uri.toString();
                                    rootRef.child("createdEvents").child(currentUserID).child("image")
                                            .setValue(downloadUri)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CreateEventActivity.this, "Image Save Successful", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    } else {
                                                        String message = task.getException().toString();
                                                        Toast.makeText(CreateEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(CreateEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }

    String getCurrentTimestamp() {
        Date date = new Date();
        long time = date.getTime();

        Timestamp timestamp = new Timestamp(time);

        return timestamp.toString();
    }

    private void createEvent() {
        String setEventTitle = eventTitle.getText().toString();
        final String setEventCategory = eventCategory.getText().toString();
        final String setEventTime = eventTime.getText().toString();
        final String setEventLocation = eventLocation.getText().toString();

        if (TextUtils.isEmpty(setEventTitle)){
            Toast.makeText(this, "Please, enter title of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventCategory)){
            Toast.makeText(this, "Please, enter category of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventTime)){
            Toast.makeText(this, "Please, enter time of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventLocation)){
            Toast.makeText(this, "Please, enter location of the event", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> eventMap = new HashMap<>();
            eventMap.put("uid", currentUserID);
            eventMap.put("name", currentUserID);
            eventMap.put("category", currentUserID);
            eventMap.put("time", currentUserID);
            eventMap.put("location", currentUserID);

            rootRef.child("createdEvents").child(currentUserID).child("name")
                    .setValue(setEventTitle)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(CreateEventActivity.this, "Image Save Successful", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
                                rootRef.child("createdEvents").child(currentUserID).child("category")
                                        .setValue(setEventCategory)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(CreateEventActivity.this, "Image Save Successful", Toast.LENGTH_SHORT).show();
//                                                    loadingBar.dismiss();
                                                    rootRef.child("createdEvents").child(currentUserID).child("time")
                                                            .setValue(setEventTime)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
//                                                                        Toast.makeText(CreateEventActivity.this, "Image Save Successful", Toast.LENGTH_SHORT).show();
//                                                                        loadingBar.dismiss();
                                                                        rootRef.child("createdEvents").child(currentUserID).child("location")
                                                                                .setValue(setEventLocation)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            sendUserToHomeActivity();
                                                                                            Toast.makeText(CreateEventActivity.this, "Event Save Successful", Toast.LENGTH_SHORT).show();
                                                                                            loadingBar.dismiss();
                                                                                        } else {
                                                                                            String message = task.getException().toString();
                                                                                            Toast.makeText(CreateEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                                            loadingBar.dismiss();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    } else {
                                                                        String message = task.getException().toString();
                                                                        Toast.makeText(CreateEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    String message = task.getException().toString();
                                                    Toast.makeText(CreateEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(CreateEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });


        }

    }

    private void sendUserToHomeActivity() {
        Intent homeIntent = new Intent(CreateEventActivity.this,HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    private String showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
                        Log.v("TAG", "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        datePickerDialog.setCanceledOnTouchOutside(false);
//        datePickerDialog.
        datePickerDialog.show();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
//        eventTime.setText(dateFormat.format(date.getTime())); //.toString()
//        eventTime.setText(dateFormat.format(date));
        return dateFormat.format(date.getTime());
    }
}
