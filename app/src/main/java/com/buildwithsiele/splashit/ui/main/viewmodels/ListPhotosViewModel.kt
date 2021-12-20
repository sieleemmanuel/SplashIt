package com.buildwithsiele.splashit.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService
import com.buildwithsiele.splashit.data.repositories.MainRepository
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class ListImagesViewModel(photosDatabase: PhotosDatabase, apiService: ApiService): ViewModel() {
    //reference to repository

    init {
        updateDataFromRepository()
    }
    private val photosRepository = MainRepository(photosDatabase,apiService)
    private fun updateDataFromRepository(){
        viewModelScope.launch {
            try {
                photosRepository.updatePhotoList()
            }catch (e:Exception){
                Log.d("ViewModel", "Error: ${e.message} ")
            }

        }
    }

    fun fetchPhotosLiveData():LiveData<PagingData<Photo>>{
        return photosRepository.getPhotosResultsStream()
            .cachedIn(viewModelScope)

    }

}
@ExperimentalPagingApi
class ListImagesViewModelFactory(private val photosDatabase: PhotosDatabase, val apiService: ApiService):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListImagesViewModel::class.java)) {
            return ListImagesViewModel(photosDatabase,apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}