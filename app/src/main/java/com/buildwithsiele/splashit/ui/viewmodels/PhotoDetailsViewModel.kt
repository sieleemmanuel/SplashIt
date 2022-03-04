package com.buildwithsiele.splashit.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.network.PhotosApi
import com.buildwithsiele.splashit.data.repositories.MainRepository

@ExperimentalPagingApi
class PhotoDetailsViewModel(photosDatabase:PhotosDatabase) : ViewModel() {
    //reference to repository
   private val apiService = PhotosApi.apiService
    private val photosRepository = MainRepository(photosDatabase,apiService)
    val photosList = photosRepository.photoList

}
@ExperimentalPagingApi
class PhotoDetailsViewModelFactory(private val photosDatabase: PhotosDatabase, private val  context: Context):ViewModelProvider.Factory{
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
     if(modelClass.isAssignableFrom(PhotoDetailsViewModel::class.java)){
         return PhotoDetailsViewModel(photosDatabase) as T
     }
     throw IllegalArgumentException("Unknown ViewModel Class")
 }

}