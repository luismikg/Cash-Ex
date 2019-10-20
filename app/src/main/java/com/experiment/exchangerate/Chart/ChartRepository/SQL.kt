package com.experiment.exchangerate.Chart.ChartRepository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQL(context: Context):
    SQLiteOpenHelper(context, NAME_DB, null, DATABASE_VER) {

    companion object {
        val DATABASE_VER = 1
        val NAME_DB = "cahe_cash_ex.db"
        val NAME_TABLE = "cahe"
        val CREATE_TABLE_CACHE =
            "CREATE TABLE IF NOT EXISTS ${SQL.NAME_TABLE} (fecha_inicio VARCHAR, fecha_fin VARCHAR, json VARCHAR)"
    }

    fun SELECT(fecha_inicio:String, fecha_fin:String): String?{
        var db:SQLiteDatabase = this@SQL.writableDatabase
        val select = "SELECT json FROM ${SQL.NAME_TABLE} WHERE fecha_inicio='$fecha_inicio' AND fecha_fin='$fecha_fin'"
        val cursor:Cursor = db.rawQuery( select, null )

        if (cursor.moveToFirst()){
            return cursor.getString( 0 )
        }else{
            return null
        }
    }

    fun INSERT_IF_NOT_EXIST(fecha_inicio:String, fecha_fin:String, json:String){
        val isHere:String? = this@SQL.SELECT(fecha_inicio, fecha_fin)
        if ( isHere==null ){
            this@SQL.INSERT( fecha_inicio, fecha_fin, json)
        }
    }

    private fun INSERT(fecha_inicio:String, fecha_fin:String, json:String) {
        var db:SQLiteDatabase = this@SQL.writableDatabase
        val insert:String = "INSERT INTO ${SQL.NAME_TABLE} (fecha_inicio, fecha_fin, json) VALUES('$fecha_inicio', '$fecha_fin', '$json')"
        db.execSQL( insert )
    }

    override fun onCreate(database: SQLiteDatabase?) {
        database?.let {
            it.execSQL( SQL.CREATE_TABLE_CACHE )
        }
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database?.let {
            onCreate(it)
        }
    }

}