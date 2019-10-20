package com.experiment.exchangerate.Date

import android.app.DatePickerDialog
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.experiment.exchangerate.Activitys.ChartActivity
import com.experiment.exchangerate.Helper.OnDateClickListener
import com.experiment.exchangerate.R
import java.util.*
import java.text.SimpleDateFormat

class DateViewModel: ViewModel(){

    companion object{
        var callbackChangeDate: OnDateClickListener? = null
    }


    //Observables
    private var observableStartDate:MutableLiveData<String>? = null
    private var observableEndDate:MutableLiveData<String>? = null

    init {
        this@DateViewModel.observableStartDate = MutableLiveData()
        this@DateViewModel.observableEndDate = MutableLiveData()

        this@DateViewModel.initDates()
    }

    //services methods:
    private fun initDates() {

        this@DateViewModel.initStartDate()
        this@DateViewModel.initEndDate()
    }

    private fun initEndDate(){
        val calendar:Calendar = Calendar.getInstance()

        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        month = month + 1
        var strMonth = month.toString()
        if (strMonth.length==1) {
            strMonth = "0"+strMonth
        }
        var strDate:String =  "$year-$strMonth-$day"


        this@DateViewModel.changeEndDate( strDate )
        this@DateViewModel.setDateInDateModel(strEndDate=strDate)
    }

    private fun initStartDate(){
        val calendar:Calendar = Calendar.getInstance()

        //up to one year less
        calendar.add(Calendar.DAY_OF_MONTH, -8)

        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        month = month + 1
        var strMonth = month.toString()
        if (strMonth.length==1) {
            strMonth = "0"+strMonth
        }

        var strDate =  "$year-$strMonth-$day"

        this@DateViewModel.changeStartDate( strDate )
        this@DateViewModel.setDateInDateModel(strStartDate=strDate)
    }

    private fun getCalendarFromDate( strDate:String? ): Calendar{

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -8)

        strDate?.let {
            val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = simpleDateFormat.parse( it )
            calendar.time = date
        }

        return calendar
    }

    private fun setMinAndMaxDateToStartDate( dataPickerDialog:DatePickerDialog ){
        val calendar:Calendar = Calendar.getInstance()

        //Max date
        dataPickerDialog.datePicker.maxDate = calendar.timeInMillis

        //Min date
        calendar.add(Calendar.YEAR, -1)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        dataPickerDialog.datePicker.minDate = calendar.timeInMillis
    }

    private fun setMinAndMaxDateToEndDate( dataPickerDialog:DatePickerDialog, strStarDate:String? ){
        var calendar:Calendar = Calendar.getInstance()
        calendar = this@DateViewModel.getCalendarFromDate( strStarDate )

        //Min date
        calendar = this@DateViewModel.getCalendarFromDate( strStarDate )
        dataPickerDialog.datePicker.minDate = calendar.timeInMillis

        //Max date
        calendar.add(Calendar.DAY_OF_MONTH, +8)
        dataPickerDialog.datePicker.maxDate = calendar.timeInMillis
    }

    private fun setDateInDateModel( strStartDate:String?=null, strEndDate:String?=null){

        var dateModel: DateModel = DateModel.instance

        strStartDate?.let {
            dateModel.startDate = it
        }

        strEndDate?.let {
            dateModel.endDate = it
        }
    }

    private fun changeStartDate(strDate:String) {
        this@DateViewModel.observableStartDate?.let {
            it.value = strDate
        }
        DateViewModel.callbackChangeDate?.let {
            it.onDateSelected(strStartDate = strDate)
        }
    }

    private fun changeEndDate(strDate:String) {
        this@DateViewModel.observableEndDate?.let {
            it.value = strDate
        }
        DateViewModel.callbackChangeDate?.let {
            it.onDateSelected(strEndDate = strDate)
        }
    }

    //ViewModel methods:
    fun onClickStartDate(dateView: Fragment){

        //Show the date selected
        var calendar:Calendar? = null
        this@DateViewModel.observableStartDate?.let {
            calendar = this@DateViewModel.getCalendarFromDate( it.value )
        }
        if ( calendar==null ) {
            calendar = Calendar.getInstance()
            calendar!!.add(Calendar.DAY_OF_MONTH, -8)
        }

        var year = calendar!!.get(Calendar.YEAR)
        var month = calendar!!.get(Calendar.MONTH)
        var day = calendar!!.get(Calendar.DAY_OF_MONTH)

        dateView.activity?.let {
            val dataPickerDialog = DatePickerDialog(it, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                year = i
                month = (i2+1)
                day = i3

                val strDate =  "$year-$month-$day"

                this@DateViewModel.changeStartDate( strDate )
                this@DateViewModel.changeEndDate( strDate )
                this@DateViewModel.setDateInDateModel(strStartDate=strDate, strEndDate=strDate)
                Toast.makeText(it, R.string.warning_end_date_changed_too, Toast.LENGTH_LONG).show()

            }, year, month, day)

            this@DateViewModel.setMinAndMaxDateToStartDate( dataPickerDialog )

            dataPickerDialog.show()
        }
    }

    fun onClickEndtDate(dateView: Fragment){

        //Show the date selected
        var calendar:Calendar? = null
        this@DateViewModel.observableEndDate?.let {
            calendar = this@DateViewModel.getCalendarFromDate( it.value )
        }
        if ( calendar==null ) {
            calendar = Calendar.getInstance()
        }

        var year = calendar!!.get(Calendar.YEAR)
        var month = calendar!!.get(Calendar.MONTH)
        var day = calendar!!.get(Calendar.DAY_OF_MONTH)

        dateView.activity?.let {
            val dataPickerDialog = DatePickerDialog(it, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                year = i
                month = (i2+1)
                day = i3

                val strDate:String =  "$year-$month-$day"

                this@DateViewModel.changeEndDate(strDate)
                this@DateViewModel.setDateInDateModel(strEndDate=strDate)

            }, year, month, day)

            this@DateViewModel.observableStartDate?.let {
                this@DateViewModel.setMinAndMaxDateToEndDate( dataPickerDialog, it.value )
            }

            dataPickerDialog.show()
        }
    }

    fun onClickBtnNext(dateView: Fragment){

        var startDate:String = String()
        var endDate:String = String()

        this@DateViewModel.observableStartDate?.let {
            it.value?.let {
                startDate = it
            }
        }

        this@DateViewModel.observableEndDate?.let {
            it.value?.let {
                endDate = it
            }
        }

        this@DateViewModel.setDateInDateModel(strStartDate=startDate, strEndDate=endDate)

        dateView.activity?.let {
            val intent = Intent(it, ChartActivity::class.java)
            it.startActivity(intent)
            it.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    //Observable
    fun getStartDate(): LiveData<String>? {
        this@DateViewModel.observableStartDate?.let {
            return it
        }
        return null
    }

    //Observable
    fun getEndDate(): LiveData<String>? {
        this@DateViewModel.observableEndDate?.let {
            return it
        }
        return null
    }
}
