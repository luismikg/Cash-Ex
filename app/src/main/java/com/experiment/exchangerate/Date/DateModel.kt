package com.experiment.exchangerate.Date

import com.github.mikephil.charting.data.Entry

class DateModel(startDate:String, endDate:String) {

    //Singleton
    companion object{
        val instance: DateModel = DateModel(String(), String())
    }

    var startDate: String
    var endDate: String

    init {
        this@DateModel.startDate = startDate
        this@DateModel.endDate = startDate
    }
}