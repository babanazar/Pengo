package com.example.pengout.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Button createEventButton;
    private EditText eventTitle, eventDesc, eventCategory, eventLocation;
    private TextView eventTime;
    ImageView eventImage;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private static final int GALLERYPICK = 1;
    private StorageReference eventImageRef;

    private ProgressDialog loadingBar;

    private Toolbar settingsToolbar;

    private Uri resultUri;

    private CheckBox isPrivate;

    Calendar now = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private String currentEventId, imageDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        settingsToolbar = findViewById(R.id.create_toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Create Event");

        currentEventId = String.valueOf(System.currentTimeMillis());
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        eventImageRef = FirebaseStorage.getInstance().getReference().child("Event Images");

        isPrivate = findViewById(R.id.isPrivateCheckBox);

        datePickerDialog = DatePickerDialog.newInstance(
                CreateEventActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );


        timePickerDialog = TimePickerDialog.newInstance(
                CreateEventActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                false
        );

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
                datePickerDialog.show(getFragmentManager(), "CreateEventActivity:Datetimepickerdialog");
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
        eventDesc = findViewById(R.id.event_desc);
        eventCategory = findViewById(R.id.event_category);
        eventTime = findViewById(R.id.event_time);
        eventLocation = findViewById(R.id.event_location);

        eventImage = findViewById(R.id.set_event_image);


        loadingBar = new ProgressDialog(this);


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

                resultUri = result.getUri();
                final StorageReference filePath = eventImageRef.child(currentUserID + "_" + currentEventId);

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateEventActivity.this, "Event Image Is Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUri = uri.toString();
                                    imageDownloadUrl = downloadUri;
                                    rootRef.child("createdEvents").child(currentUserID).child(currentEventId).child("url")
                                            .setValue(downloadUri)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CreateEventActivity.this, "Image Save Successful", Toast.LENGTH_SHORT).show();
                                                        Picasso.get().load(resultUri).into(eventImage);
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


    private void createEvent() {
        String setEventTitle = eventTitle.getText().toString();
        final String setEventCategory = eventCategory.getText().toString();
        final String setEventTime = eventTime.getText().toString();
        final String setEventLocation = eventLocation.getText().toString();
        final String setEventDesc = eventDesc.getText().toString();

        if (TextUtils.isEmpty(setEventTitle)) {
            Toast.makeText(this, "Please, enter title of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventCategory)) {
            Toast.makeText(this, "Please, enter category of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventTime)) {
            Toast.makeText(this, "Please, enter time of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventLocation)) {
            Toast.makeText(this, "Please, enter location of the event", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setEventDesc)) {
            Toast.makeText(this, "Please, enter description of the event", Toast.LENGTH_SHORT).show();
        }

        else {


            if (!isPrivate.isChecked()){
                HashMap<String, Object> myEvent = new HashMap();

                myEvent.put("category", setEventCategory);
                myEvent.put("count", 0);
                myEvent.put("date", " " + setEventTime);
                myEvent.put("desc", setEventDesc);
                myEvent.put("link", "");
                myEvent.put("place", setEventLocation);
                myEvent.put("name", setEventTitle);
                myEvent.put("time", "");
                myEvent.put("url", imageDownloadUrl);

                rootRef.child("newEvents").push().updateChildren(myEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CreateEventActivity.this, "Added to new events", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            rootRef.child("createdEvents").child(currentUserID).child(currentEventId).child("name")
                    .setValue(setEventTitle)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                rootRef.child("createdEvents").child(currentUserID).child(currentEventId).child("category")
                                        .setValue(setEventCategory)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    rootRef.child("createdEvents").child(currentUserID).child(currentEventId).child("time")
                                                            .setValue(setEventTime)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        rootRef.child("createdEvents").child(currentUserID).child(currentEventId).child("location")
                                                                                .setValue(setEventLocation)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            rootRef.child("createdEvents").child(currentUserID).child(currentEventId).child("isPrivate")
                                                                                                    .setValue(isPrivate.isChecked())
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            sendUserToHomeActivity();
                                                                                                            Toast.makeText(CreateEventActivity.this, "Event Save Successful", Toast.LENGTH_SHORT).show();
                                                                                                            loadingBar.dismiss();
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
        Intent homeIntent = new Intent(CreateEventActivity.this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timePickerDialog.show(getFragmentManager(), "KreateAktiwiti:Time piker daylog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        eventTime.setText(now.getTime().toString());
    }


}
