package com.example.runtracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runtracker.R
import com.example.runtracker.database.RunDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(nav_host_fragment.findNavController())

        nav_host_fragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.setupFragment, R.id.trackingFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }


        }




    }
}