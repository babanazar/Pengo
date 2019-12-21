package com.example.pengout.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.utils.FragmentChangeListener;
import com.example.pengout.view.activity.InformationActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class InfoFragment2 extends Fragment {

    Button nextButton;
    CircleImageView userImage;
    private static final int GALLERYPICK = 1;
    private ProgressDialog loadingBar;
    private StorageReference userProfileImagesRef;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private InformationActivity nActivity;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info2,container,false);
        nextButton = view.findViewById(R.id.nextButton);
        userImage = (CircleImageView) view.findViewById(R.id.infoImage);
        nActivity = (InformationActivity) getActivity();

        userProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        loadingBar = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();




        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                nActivity.startActivityForResult(galleryIntent, GALLERYPICK);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();
                nextFragment();
            }
        });
        return view;
    }

    void nextFragment(){
        InfoFragment3 fragment3 = new InfoFragment3();
        FragmentChangeListener fcl = (FragmentChangeListener)getActivity();
        fcl.goFragment(fragment3);
    }




}