package com.experiment.exchangerate.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.experiment.exchangerate.R

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let {
            it.hide()
        }
        setContentView(R.layout.activity_chart)
    }
}