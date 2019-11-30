package com.example.pengout.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.example.pengout.view.adapter.ChatTabsAccessorAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private ChatTabsAccessorAdapter myChatTabsAccessorAdapter;
    private DatabaseReference rootRef;
    private Context mContext = ChatActivity.this;
//    FirebaseUser currentUser;
//
//    CircleImageView profile_image;
//    TextView username;

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
//    Firebase reference1, reference2;

    private static final int ACTIVITY_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("inHomeActivity", "success");
        setContentView(R.layout.activity_chat);


//        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        mToolbar.setTitle("Chats");


        rootRef = FirebaseDatabase.getInstance().getReference();
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Pengout");

        setupBottomNavigationView();

        myViewPager = findViewById(R.id.main_tabs_pager);
        myChatTabsAccessorAdapter = new ChatTabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myChatTabsAccessorAdapter);


        myTabLayout = findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

    }
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(ChatActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatActivity.this, RegisterActivity.class));
                finish();
                return true;

            case R.id.settings:
                sendUserToSettingsActivity();

            case R.id.chat_create_group:
                requestNewGroup();

            case R.id.find_friends:
                sendUserToFindFriendsActivity();
        }
        return false;
    }

    private void requestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name : ");

        final EditText groupNameField = new EditText(ChatActivity.this);
        groupNameField.setHint("e.g. Yesterday's Concert");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();

                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(ChatActivity.this, "Please Write Group Name...", Toast.LENGTH_SHORT).show();
                }

                else{
                    createNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createNewGroup(final String groupName) {
        rootRef.child("groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ChatActivity.this, groupName + " group is created successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(ChatActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void sendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(ChatActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
        finish();
    }
}
