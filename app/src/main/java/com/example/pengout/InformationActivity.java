package com.example.pengout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String USER_NAME = "com.example.pengout.username";
    public static final String USER_ID = "com.example.pengout.userid";

    //view objects
    EditText editTextName;
    Spinner spinnerGenre;
    Button buttonAddArtist;
    ListView listViewArtists;

    //a list to store all the artist from firebase database
    List<User> users;

    //our database reference object
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //getting the reference of artists node
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //getting views
        editTextName =  findViewById(R.id.editTextName);
        spinnerGenre =  findViewById(R.id.spinnerInterests);
        listViewArtists =  findViewById(R.id.listViewUsers);

        buttonAddArtist =  findViewById(R.id.buttonGoChat);

        //list to store artists
        users = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addUser();
            }
        });
    }

    /*
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */
    private void addUser() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String interest = spinnerGenre.getSelectedItem().toString();
        String imageURL = "sampleURL";
        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseUsers.push().getKey();

            //creating an Artist Object
            User user = new User(id, name, interest, imageURL);

            //Saving the Artist
            assert id != null;
            databaseUsers.child(id).setValue(user);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
