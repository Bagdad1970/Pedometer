package io.github.bagdad1970.pedometer

import android.app.Application

class PedometerApplication : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
    }
}