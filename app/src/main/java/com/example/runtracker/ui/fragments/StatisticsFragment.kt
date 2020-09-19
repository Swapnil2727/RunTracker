package com.example.runtracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.runtracker.R
import com.example.runtracker.other.CustomMarkerView
import com.example.runtracker.other.TrackingUtility
import com.example.runtracker.ui.viewModel.MainViewModel
import com.example.runtracker.ui.viewModel.StatisticsViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment: Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBarChart()
        subscribeToObservers()

    }

    private fun setUpBarChart(){
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = true
            legend.form = Legend.LegendForm.LINE
        }
    }
    private fun subscribeToObservers(){

        viewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTimeRun
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                val km = it/ 1000f
                val totalDistance = round(km* 10f)/10f
                val totalDistanceString = "${totalDistance}Km"
                tvTotalDistance.text = totalDistanceString

            }
        })

        viewModel.totalAverageSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val avgSpeed = round(it*10f)/10f
                val avgSpeedString = "${avgSpeed}Km/h"
                tvAverageSpeed.text = avgSpeedString
            }
        })

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalCalories = "${it}Kcal"
                tvTotalCalories.text = totalCalories
            }
        })

        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
            it?.let {

                val allAvgSpeed = it.indices.map {
                    i-> BarEntry(i.toFloat(),it[i].avgSpeedInKMH)
                }

                val barDataset = BarDataSet(allAvgSpeed,"Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(),R.color.colorAccent)
                }
                barChart.data = BarData(barDataset)
                barChart.marker = CustomMarkerView(it.reversed(),requireContext(),R.layout.marker_view)
                barChart.invalidate()
            }
        })
    }


}