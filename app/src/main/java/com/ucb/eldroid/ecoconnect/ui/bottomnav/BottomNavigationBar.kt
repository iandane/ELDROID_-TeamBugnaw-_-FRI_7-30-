package com.ucb.eldroid.ecoconnect.ui.bottomnav

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.ucb.eldroid.ecoconnect.R
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.crowdfunding.CrowdFundingFragment
import com.ucb.eldroid.ecoconnect.ui.bottomnav.dashboard.dashboard_fragment.DashboardFragment
import com.ucb.eldroid.ecoconnect.ui.bottomnav.home.NewsFeedFragment
import com.ucb.eldroid.ecoconnect.ui.bottomnav.profile.ProfileFragment

class BottomNavigationBar : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_bar)

        bottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNavigationView.setOnItemSelectedListener { menu ->
            when(menu.itemId){
                R.id.fund -> {
                    replaceFragment(CrowdFundingFragment())
                    true
                }R.id.dashboard -> {
                replaceFragment(DashboardFragment())
                true
            }R.id.profile -> {
                replaceFragment(ProfileFragment())
                true
            }R.id.feed -> {
                replaceFragment(NewsFeedFragment())
                true
            }
                else -> false
            }
        }
        replaceFragment(DashboardFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.navHost, fragment).commit()
    }
}