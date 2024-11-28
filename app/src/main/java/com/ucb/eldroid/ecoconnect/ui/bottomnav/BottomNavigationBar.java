package com.ucb.eldroid.ecoconnect.ui.bottomnav;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ucb.eldroid.ecoconnect.R;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CrowdFundingFragment;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment.DashboardFragment;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.home.NewsFeedFragment;
import com.ucb.eldroid.ecoconnect.ui.bottomnav.profile.ProfileFragment;

public class BottomNavigationBar extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(menu -> {
            int itemId = menu.getItemId();

            if (itemId == R.id.fund) {
                replaceFragment(new CrowdFundingFragment());
                return true;
            } else if (itemId == R.id.dashboard) {
                replaceFragment(new DashboardFragment());
                return true;
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
                return true;
            } else if (itemId == R.id.feed) {
                replaceFragment(new NewsFeedFragment());
                return true;
            }
            return false;
        });

        replaceFragment(new DashboardFragment());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navHost, fragment)
                .commit();
    }
}