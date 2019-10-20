package com.experiment.exchangerate.Activitys.Date

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.ViewModel
import com.experiment.exchangerate.Chart.ChartView
import com.experiment.exchangerate.Date.DateView
import com.experiment.exchangerate.Helper.OnDateClickListener
import com.experiment.exchangerate.R

class DateActivityViewModel: ViewModel() {

    companion object{
        var isTablet:Boolean = false
    }

    fun initActivity( dateActivity: Activity ){

        if (dateActivity.findViewById<View>(R.id.chart_fragmente) != null){
            dateActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            DateActivityViewModel.isTablet = true
            val dateView:View = dateActivity.findViewById<View>(R.id.data_fragmente)
            val btnNext = dateView.findViewById<ImageButton>(R.id.idBtnNext)
            btnNext.visibility = View.GONE
        }
        else {
            dateActivity.requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
    }

    fun onDateSelected(hostActivity: DateActivity, strStartDate: String?, strEndDate: String?) {

        if (! DateActivityViewModel.isTablet) {
            return
        }
        //It is a tablect
        //Creamos una nueva clase del fragmento y reemplazamos el que existe
        val newChartFragment = ChartView()
        val transaction = hostActivity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.chart_fragmente, newChartFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onAttachFragment(onDateClickListenerCallback: OnDateClickListener) {
        DateView.setOnDateClickListener( onDateClickListenerCallback )
    }
}