package com.buildwithsiele.splashit.ui.main.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.buildwithsiele.splashit.R

class PhotoDetailsViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("UseCompatLoadingForDrawables")
    val imageList = mutableListOf(
        R.drawable.splash_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_search_black_24dp
    )
}