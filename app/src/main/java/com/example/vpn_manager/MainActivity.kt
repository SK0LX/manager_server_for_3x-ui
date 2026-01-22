package com.example.vpn_manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vpn_manager.app.App
import com.example.vpn_manager.app.di.AppComponent

class MainActivity : AppCompatActivity() {
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).appComponent.inject(this)
    }
}