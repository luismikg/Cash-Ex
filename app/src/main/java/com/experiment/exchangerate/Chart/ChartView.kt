package com.experiment.exchangerate.Chart

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.experiment.exchangerate.Helper.ViewDialog
import com.experiment.exchangerate.R
import com.github.mikephil.charting.data.LineData

class ChartView: Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val rootView: View = inflater!!.inflate(R.layout.chart, container, false)

        this@ChartView.initView( rootView )
        return rootView
    }

    private fun initView( rootView:View ) {

        val chartViewModel: ChartViewModel = ViewModelProviders.of(this@ChartView).get(ChartViewModel::class.java)

        //observer
        val txtAverage: TextView = rootView.findViewById(R.id.idTxtAverag) as TextView
        val observerAverage = Observer<String> { strAverage -> txtAverage.text = strAverage  }
        //Set observable
        chartViewModel.getAverage()?.let {
            it.observe(this@ChartView, observerAverage)
        }

        //observer
        val txtLowestExpense: TextView = rootView.findViewById(R.id.idTxtLowestExpense) as TextView
        val observerLowestExpense = Observer<String> { strLowestExpense -> txtLowestExpense.text = strLowestExpense }
        //Set observable
        chartViewModel.getLowestExpense()?.let {
            it.observe(this@ChartView, observerLowestExpense)
        }

        //observer
        val txtHighestExpense: TextView = rootView.findViewById(R.id.idTxtHighestExpense) as TextView
        val observerHighestExpense = Observer<String> { strHighestExpense -> txtHighestExpense.text = strHighestExpense }
        //Set observable
        chartViewModel.getHighestExpense()?.let {
            it.observe(this@ChartView, observerHighestExpense)
        }

        //observer
        val lineChart: com.github.mikephil.charting.charts.LineChart = rootView.findViewById(R.id.idLineChart) as com.github.mikephil.charting.charts.LineChart
        val observerLineChart = Observer<LineData> { dataLineChart ->
            lineChart.data = dataLineChart
            lineChart.invalidate()
        }
        //Set observable
        chartViewModel.getLineChart()?.let {
            it.observe(this@ChartView, observerLineChart)
        }

        this@ChartView.activity?.let {
            val act:Activity= it
            val viewDialog = ViewDialog(it)
            this@ChartView.context?.let {
                chartViewModel.initChart( this@ChartView, viewDialog, it )
            }
        }
    }
}