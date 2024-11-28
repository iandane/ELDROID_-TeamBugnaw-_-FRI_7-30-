package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ucb.eldroid.ecoconnect.R;


public class CrowdFundingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crowd_funding, container, false);
    }
}