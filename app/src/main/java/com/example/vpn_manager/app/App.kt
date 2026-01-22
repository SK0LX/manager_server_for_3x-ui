package com.example.vpn_manager.app

import android.app.Application
import com.example.vpn_manager.app.di.AppComponent
import com.example.vpn_manager.app.di.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }
}