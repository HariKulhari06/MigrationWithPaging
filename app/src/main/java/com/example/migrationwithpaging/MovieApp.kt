package com.example.migrationwithpaging

import android.app.Application
import com.example.migrationwithpaging.data.AppDataBase

class MovieApp : Application() {
    lateinit var appDataBase: AppDataBase

    override fun onCreate() {
        super.onCreate()
        appDataBase = AppDataBase.getDatabase(applicationContext)!!
    }
}