package com.ucb.eldroid.ecoconnect.ui.bottomnav.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ucb.eldroid.ecoconnect.R;

public class ProfileFragment extends Fragment {

    private Button editProfileButton;
    private View accountInfoButton, favoriteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        editProfileButton = view.findViewById(R.id.editProfileBtn);
        accountInfoButton = view.findViewById(R.id.accountBtn);
        favoriteButton = view.findViewById(R.id.favbtn);

        // Set up button listeners
        setupButtonListeners();

        return view;
    }

    private void setupButtonListeners() {
        // Account Info button
        accountInfoButton.setOnClickListener(v -> {
            // Navigate to AccountInfoFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHost, new AccountInfoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            // Navigate to EditProfile activity
            Intent intent = new Intent(requireContext(), EditProfile.class);
            startActivity(intent);
        });

        // Favorites button
        favoriteButton.setOnClickListener(v -> {
            // Navigate to FavoriteFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHost, new FavoriteFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
