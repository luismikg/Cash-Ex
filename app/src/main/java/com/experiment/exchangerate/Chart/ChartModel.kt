package com.experiment.exchangerate.Chart

class ChartModel(strDate:String, rateValue:Float){

    val date:String
    val rate:Float

    init {
        date = strDate
        rate = rateValue
    }
}