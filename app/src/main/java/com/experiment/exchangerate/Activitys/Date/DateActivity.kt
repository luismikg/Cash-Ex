package com.experiment.exchangerate.Activitys.Date

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.experiment.exchangerate.Helper.OnDateClickListener
import com.experiment.exchangerate.R

class DateActivity : AppCompatActivity(), OnDateClickListener {

    companion object{
        const val STORAGE_REQUEST_CODE = 10
    }
    lateinit var dateActivityViewModel: DateActivityViewModel


    override fun onDateSelected(strStartDate: String?, strEndDate: String?) {
        this@DateActivity.dateActivityViewModel.onDateSelected(this, strStartDate, strEndDate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let {
            it.hide()
        }
        setContentView(R.layout.activity_date)

        this@DateActivity.checkPermissions()
        this@DateActivity.initView()
    }

    fun initView(){
        val dateActivityViewModel: DateActivityViewModel = ViewModelProviders.of(this).get(DateActivityViewModel::class.java)
        this@DateActivity.dateActivityViewModel = dateActivityViewModel
        this@DateActivity.dateActivityViewModel.onAttachFragment( this )
        dateActivityViewModel.initActivity( this )
    }

    //Oly check permissions
    private fun checkPermissions(){
        if ( (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ){
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE), DateActivity.STORAGE_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty() or (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
            checkPermissions()
        } else {
        }
    }
}
