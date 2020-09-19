package com.example.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.runtracker.R
import com.example.runtracker.other.Constants.KEY_NAME
import com.example.runtracker.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.fragment_settings) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPref()
            if(success){
                Snackbar.make(view,"Changes Saved Successfully", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view,"Please Check Input", Snackbar.LENGTH_LONG).show()
            }

        }
    }

    private fun applyChangesToSharedPref(): Boolean{
        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if(name.isEmpty()||weight.isEmpty()){
            return false
        }
        sharedPreferences.edit()
            .putString(KEY_NAME,name).putFloat(KEY_WEIGHT,weight.toFloat())
            .apply()

        val toolbarText = "Let's go, $name"
        requireActivity().tvToolbarTitle.text = toolbarText

        return true
    }


    private fun loadFromSharedPref(){
        val name = sharedPreferences.getString(KEY_NAME,"")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT,80f)
        etName.setText(name)
        etWeight.setText(weight.toString())

    }
}