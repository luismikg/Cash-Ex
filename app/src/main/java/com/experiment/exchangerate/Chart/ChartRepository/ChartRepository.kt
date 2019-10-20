package com.experiment.exchangerate.Chart.ChartRepository

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.experiment.exchangerate.Chart.ChartModel
import com.experiment.exchangerate.Chart.ChartViewModel
import com.experiment.exchangerate.Date.DateModel
import org.json.JSONObject
import kotlin.collections.ArrayList

class ChartRepository {

    val apiKey:String = "6b01dd7fa0aa8b8f358c"

    fun getData(chartViewModel: ChartViewModel, context: Context ) {

        val startDate = DateModel.instance.startDate
        val endDate = DateModel.instance.endDate
        val url = "https://free.currconv.com/api/v7/convert?q=USD_EUR,EUR_USD&compact=ultra&date=$startDate&endDate=$endDate&apiKey=$apiKey"

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->

                //Success
                val response: String = "%s".format(response.toString())
                //Save into cache
                SQL(context).INSERT_IF_NOT_EXIST( startDate, endDate, response )

                this@ChartRepository.success( response, chartViewModel )
            },
            Response.ErrorListener { error ->

                // TODO: Handle error
                //SEARCH INTO SQL CACHE
                var strJson:String? = SQL(context).SELECT( startDate, endDate )
                this@ChartRepository.error( strJson, chartViewModel )

            }
        )

        queue.add(jsonObjectRequest)
    }

    fun error( strJson:String?, chartViewModel:ChartViewModel ){
        if (strJson==null){
            chartViewModel.callbackError()
        } else {
            this@ChartRepository.success( strJson, chartViewModel )
        }
    }

    fun success(strJson:String, chartViewModel:ChartViewModel ){
        val arrChartModel: ArrayList<ChartModel> = getRate( strJson )
        chartViewModel.callbackSuccess( arrChartModel )
    }

    fun getRate( strJson:String ): ArrayList<ChartModel>   {
        val json:JSONObject = JSONObject( strJson )
        val keys = (json["USD_EUR"] as JSONObject).keys()
        val arrChartModel: ArrayList<ChartModel> = ArrayList()

        for (key:String in keys){
            val rate:Double = (json["USD_EUR"] as JSONObject)[key] as Double
            val chartModel = ChartModel(key, rate.toFloat())
            arrChartModel.add( chartModel )
        }

        return arrChartModel
    }

}

