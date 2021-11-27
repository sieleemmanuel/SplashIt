package com.buildwithsiele.splashit.ui.main.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.buildwithsiele.splashit.R

class ImageDetailsViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("UseCompatLoadingForDrawables")
    val imageList = mutableListOf(
        R.drawable.splash_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_search_black_24dp
    )
}