package com.buildwithsiele.splashit.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.buildwithsiele.splashit.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SplashIt)
        setContentView(R.layout.activity_main)
    }
}