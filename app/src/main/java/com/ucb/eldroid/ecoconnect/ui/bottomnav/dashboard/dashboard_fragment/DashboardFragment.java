package com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Toast;

import com.ucb.eldroid.ecoconnect.R;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CreateProject;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CrowdFundingFragment;


public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        View createProjCardView = view.findViewById(R.id.CreateProjCardview);
        View crowdFundCardView = view.findViewById(R.id.crowdFundCardView);

        // Set the OnClickListener for the CardView

        createProjCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateProject.class);
                startActivity(intent);

                Toast.makeText(getActivity(), "Create A Project", Toast.LENGTH_SHORT).show();
            }
        });

        crowdFundCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a FragmentTransaction to replace the current fragment with CrowdFundingFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.navHost, new CrowdFundingFragment()) // Replace the fragment
                        .addToBackStack(null) // Add this transaction to the back stack
                        .commit(); // Commit the transaction
            }
        });
        return view;
    }
}