package com.experiment.exchangerate.Date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.experiment.exchangerate.Helper.OnDateClickListener
import com.experiment.exchangerate.R


class DateView: Fragment() {

    companion object{
        fun setOnDateClickListener( callback: OnDateClickListener){
            DateViewModel.callbackChangeDate =  callback
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val rootView:View = inflater!!.inflate(R.layout.date, container, false)
        this@DateView.initView( rootView )

        return rootView
    }

    private fun initView( rootView:View ){

        val dateViewModel:DateViewModel = ViewModelProviders.of(this@DateView).get(DateViewModel::class.java)

        //Start date calendar Actions
        val lyStartDate = rootView.findViewById<LinearLayout>(R.id.idlyStartDate)
        lyStartDate.setOnClickListener {
            dateViewModel.onClickStartDate(this@DateView )
        }

        //End date calendar Actions
        val lyEndDate = rootView.findViewById<LinearLayout>(R.id.idlyEndDate)
        lyEndDate.setOnClickListener {
            dateViewModel.onClickEndtDate(this@DateView )
        }

        //Next button Actions
        val btnNext:ImageButton = rootView.findViewById<ImageButton>(R.id.idBtnNext)
        btnNext.setOnClickListener {
            dateViewModel.onClickBtnNext(this@DateView )
        }

        //observer
        val txtStartDate: TextView = rootView.findViewById(R.id.idTxtStartDate) as TextView
        val observerStartDate = Observer<String> { txtDate -> txtStartDate.text = txtDate }
        //Set observable
        dateViewModel.getStartDate()?.let {
            it.observe(this@DateView, observerStartDate)
        }

        //observer
        val txtEndDate: TextView = rootView.findViewById(R.id.idTxtEndDate) as TextView
        val observerEndDate = Observer<String> { txtDate -> txtEndDate.text = txtDate }
        //Set observable
        dateViewModel.getEndDate()?.let {
            it.observe(this@DateView, observerEndDate)
        }
    }
}