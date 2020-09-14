package com.example.runtracker.ui.fragments

import com.example.runtracker.services.TrackingService
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.runtracker.BuildConfig
import com.example.runtracker.R
import com.example.runtracker.other.Constants
import com.example.runtracker.other.Constants.ACTION_PAUSE_SERVICE
import com.example.runtracker.other.Constants.ACTION_START_RESUME_SERVICE
import com.example.runtracker.other.Constants.ACTION_STOP_SERVICE
import com.example.runtracker.other.Constants.MAP_ZOOM
import com.example.runtracker.other.Constants.POLYLINE_COLOR
import com.example.runtracker.other.Constants.POLYLINE_WIDTH
import com.example.runtracker.other.TrackingUtility
import com.example.runtracker.services.Polyline
import com.example.runtracker.ui.viewModel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tracking.*

@AndroidEntryPoint
class TrackingFragment: Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    private var currentTimeInMillis = 0L

    private var menu:Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        btnStartRun.setOnClickListener{
            toggleRun()
        }
        mapView.getMapAsync{
            map = it
            addAllPolylines()
        }
        subscribeToObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu,menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(currentTimeInMillis> 0L){
            this.menu?.getItem(0)?.setVisible(true)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.miCancelTracking -> {
                showCancelTrackingDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelTrackingDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme)
            .setTitle("Cancel the run?")
            .setMessage("All the data will be deleted of current run!! ")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes"){_,_ ->
                stopRun()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun(){
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)

    }

    private fun subscribeToObservers(){

        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility
                .getFormattedStopWatchTime(currentTimeInMillis,true)

            tvTimer.text = formattedTime
        })
    }



    private fun toggleRun(){
        if(isTracking){
            menu?.getItem(0)?.setVisible(true)
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            btnStartRun.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        }else{
            menu?.getItem(0)?.setVisible(true)
            btnStartRun.text = "Stop"
            btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.last().last(), MAP_ZOOM))
        }
    }

    private fun addAllPolylines(){

        for(polyline in pathPoints){

            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)

            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(){

        if(pathPoints.isNotEmpty() && pathPoints.last().size> 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size-2]
            val lastLatLng  = pathPoints.last().last()

            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polyLineOptions)
        }

    }


    private fun sendCommandToService(action: String){
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }
    //map view Requires to override all the lifecycle method
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }


}

