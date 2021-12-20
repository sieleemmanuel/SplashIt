package com.buildwithsiele.splashit.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.PhotosApi
import com.buildwithsiele.splashit.data.repositories.MainRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

@ExperimentalPagingApi
class PhotoDetailsViewModel(photosDatabase:PhotosDatabase) : ViewModel() {
   /* init {
        viewModelScope.launch {
            try {
                photosRepository.updatePhotoList()
            }catch (e:Exception){
                Log.d("ViewModel", "Error: ${e.message} ")
            }

        }
    }*/
    //reference to repository
   private val apiService = PhotosApi.apiService
    private val photosRepository = MainRepository(photosDatabase,apiService)

    val photosList = fetchPhotosLiveData()

    private fun fetchPhotosLiveData(): LiveData<PagingData<Photo>> {
        return photosRepository.getPhotosResultsStream()
            .cachedIn(viewModelScope)

    }

}
@ExperimentalPagingApi
class PhotoDetailsViewModelFactory(private val photosDatabase: PhotosDatabase):ViewModelProvider.Factory{
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
     if(modelClass.isAssignableFrom(PhotoDetailsViewModel::class.java)){
         return PhotoDetailsViewModel(photosDatabase) as T
     }
     throw IllegalArgumentException("Unknown ViewModel Class")
 }

}