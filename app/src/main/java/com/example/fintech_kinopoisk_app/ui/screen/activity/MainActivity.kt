package com.example.fintech_kinopoisk_app.ui.screen.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fintech_kinopoisk_app.R
import com.example.fintech_kinopoisk_app.data.db.DatabaseHandler
import com.example.fintech_kinopoisk_app.databinding.ActivityMainBinding
import com.example.fintech_kinopoisk_app.ui.screen.fragment.InformationFragment
import com.example.fintech_kinopoisk_app.ui.screen.fragment.PopularFragment


class MainActivity: AppCompatActivity() {

    private lateinit var controller: NavController

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        controller = (supportFragmentManager.findFragmentById(R.id.container_fragment_in_activity) as NavHostFragment).navController

        binding?.apply {
            bottomNavigationFragmentMain.setupWithNavController(controller)
            setContentView(root)
        }
        DatabaseHandler.dbInitialize(applicationContext)
    }

    fun changeBtnNavVisibility(isVisible: Boolean){
        binding?.bottomNavigationFragmentMain?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


}