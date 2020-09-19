package com.example.runtracker.ui.fragments

import android.Manifest
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.runtracker.R
import com.example.runtracker.adapters.RunAdapter
import com.example.runtracker.other.Constants.KEY_NAME
import com.example.runtracker.other.Constants.REQUEST_LOCATION_PERMISSION
import com.example.runtracker.other.SortType
import com.example.runtracker.other.TrackingUtility
import com.example.runtracker.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class RunFragment: Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var runAdapter: RunAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestLocationPermission()
        setUpRecyclerView()

        val toolbarText = "Let's go, ${sharedPreferences.getString(KEY_NAME,"")}"
        requireActivity().tvToolbarTitle.text = toolbarText

        spFilter.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
               when(pos){
                   0 -> viewModel.sortRuns(SortType.DATE)
                   1 -> viewModel.sortRuns(SortType.TIME_IN_MILLIS)
                   2 -> viewModel.sortRuns(SortType.DISTANCE)
                   3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                   4 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
               }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }


    }
    private fun setUpRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
    }
    private fun requestLocationPermission(){

        if(TrackingUtility.hasLocationPermissions(requireContext())){
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this,
            "Accpet location permission to use this app",
            REQUEST_LOCATION_PERMISSION,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
        }else{

            EasyPermissions.requestPermissions(this,
                "Accpet location permission to use this app",
                REQUEST_LOCATION_PERMISSION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
           requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}