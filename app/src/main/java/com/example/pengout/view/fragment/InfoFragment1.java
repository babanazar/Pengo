package com.example.pengout.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pengout.R;
import com.example.pengout.utils.FragmentChangeListener;

public class InfoFragment1 extends Fragment {

    Button nextButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info1,container,false);
        nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment();
            }
        });
        return view;
    }

    void nextFragment(){
        InfoFragment2 fragment2 = new InfoFragment2();
        FragmentChangeListener fcl = (FragmentChangeListener)getActivity();
        fcl.goFragment(fragment2);
    }
}

