package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ucb.eldroid.ecoconnect.R;

public class AccountInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button functionality
        ImageView backButton = view.findViewById(R.id.backBtn);
        backButton.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        // Handle Delete Account Button click
        Button deleteAccountButton = view.findViewById(R.id.deleteAccount);
        deleteAccountButton.setOnClickListener(v -> {
            // Show a Toast when the delete button is clicked
            Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();

            // Optionally, you can navigate back or perform other actions
            // Example: Navigate to another fragment or show a confirmation dialog
        });
    }
}
