package com.experiment.exchangerate.Chart

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiment.exchangerate.Activitys.Date.DateActivityViewModel
import com.experiment.exchangerate.Helper.ViewDialog
import com.experiment.exchangerate.Chart.ChartRepository.ChartRepository
import com.experiment.exchangerate.R
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class ChartViewModel: ViewModel() {

    lateinit var viewDialog: ViewDialog
    var chartView: ChartView? = null

    //Observables
    private var observableAverage: MutableLiveData<String>? = null
    private var observableLowestExpense: MutableLiveData<String>? = null
    private var observableHighestExpense: MutableLiveData<String>? = null
    private var observableLineChart: MutableLiveData<LineData>? = null

    init {
        this@ChartViewModel.observableAverage = MutableLiveData()
        this@ChartViewModel.observableLowestExpense = MutableLiveData()
        this@ChartViewModel.observableHighestExpense = MutableLiveData()
        this@ChartViewModel.observableLineChart = MutableLiveData()
    }

    //services methods
    fun initChart(chartView: ChartView, dialog: ViewDialog, context: Context ) {
        this@ChartViewModel.chartView = chartView
        this@ChartViewModel.chartView?.let {
            it.activity?.let {
                this@ChartViewModel.viewDialog = ViewDialog(it)
            }
        }
        this@ChartViewModel.viewDialog.showDialog()

        //Get data
        val chartRepository: ChartRepository =
            ChartRepository()
        chartRepository.getData(this@ChartViewModel, context)
    }

    fun callbackSuccess( arrChartModel:ArrayList<ChartModel> ){

        //get Data
        val df = DecimalFormat("0.00")
        var arrData: ArrayList<Any> = this@ChartViewModel.getDataFrom( arrChartModel )
        val average:String = df.format( arrData[1] )
        val biggestRate:String = df.format( arrData[2] )
        val lowestRate:String = df.format( arrData[3] )
        val dataVal: ArrayList<Entry> = arrData[0] as ArrayList<Entry>

        val data: LineData = this@ChartViewModel.configureChart( dataVal )

        this@ChartViewModel.showAllData(data, average, biggestRate, lowestRate)
        this@ChartViewModel.viewDialog.hideDialog()
    }

    private fun getDataFrom( arrChartModel:ArrayList<ChartModel> ): ArrayList<Any> {
        val dataVals: ArrayList<Entry> = ArrayList<Entry>()

        var biggestRate:Float = 0f
        var lowestRate:Float = 10000000f
        var sum = 0f
        var idx:Float = 0f

        for (chartModel:ChartModel in arrChartModel){
            dataVals.add( Entry( idx, chartModel.rate) )
            idx += 1f

            if (chartModel.rate > biggestRate){
                biggestRate = chartModel.rate
            }
            if (chartModel.rate < lowestRate){
                lowestRate = chartModel.rate
            }

            sum += chartModel.rate
        }

        //Results
        val arrReturn = ArrayList<Any>()
        arrReturn.add(dataVals as Any)
        arrReturn.add( (sum/idx) as Any )
        arrReturn.add(biggestRate as Any)
        arrReturn.add(lowestRate as Any)
        return arrReturn
    }

    private fun configureChart( dataVal: ArrayList<Entry> ): LineData{
        val lineDataSet1 = LineDataSet( dataVal, "USD to EUR" )
        lineDataSet1.setDrawFilled(true)
        lineDataSet1.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet1.setDrawValues(false)
        lineDataSet1.fillAlpha =150
        lineDataSet1.setDrawCircles(false)

        val dataSet: ArrayList<ILineDataSet> = ArrayList()
        dataSet.add(lineDataSet1)
        return LineData(dataSet)
    }

    fun callbackError(){
        this@ChartViewModel.viewDialog.hideDialog()
        this@ChartViewModel.showAlert()
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this@ChartViewModel.chartView!!.context)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.alert_message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if( ! DateActivityViewModel.isTablet ) {
                this@ChartViewModel.chartView!!.activity!!.finish()
            }
        }
        builder.show()
    }

    //ViewModel methods:
    private fun showAllData(data: LineData, strAverage: String, strBiggestRate: String, strLowestRate: String){

        //Chart
        this@ChartViewModel.observableLineChart?.let {
            it.value = data
        }

        this@ChartViewModel.observableAverage?.let {
            it.value = "$"+strAverage
        }
        this@ChartViewModel.observableLowestExpense?.let {
            it.value = "$"+strLowestRate
        }
        this@ChartViewModel.observableHighestExpense?.let {
            it.value = "$"+strBiggestRate
        }
    }

    //Observable
    fun getAverage(): LiveData<String>? {
        this@ChartViewModel.observableAverage?.let {
            return it
        }
        return null
    }

    //Observable
    fun getLowestExpense(): LiveData<String>? {
        this@ChartViewModel.observableLowestExpense?.let {
            return it
        }
        return null
    }

    //Observable
    fun getHighestExpense(): LiveData<String>? {
        this@ChartViewModel.observableHighestExpense?.let {
            return it
        }
        return null
    }

    //Observable
    fun getLineChart(): LiveData<LineData>? {
        this@ChartViewModel.observableLineChart?.let {
            return it
        }
        return null
    }


}