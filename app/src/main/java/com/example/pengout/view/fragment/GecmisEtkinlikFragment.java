package com.example.pengout.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pengout.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GecmisEtkinlikFragment extends Fragment {


    public GecmisEtkinlikFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gecmis_etkinlik, container, false);
    }

}
