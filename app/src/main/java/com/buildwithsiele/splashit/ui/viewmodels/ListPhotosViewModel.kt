package com.buildwithsiele.splashit.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService
import com.buildwithsiele.splashit.data.repositories.MainRepository

@ExperimentalPagingApi
class ListImagesViewModel(photosDatabase: PhotosDatabase, apiService: ApiService): ViewModel() {

    private val photosRepository = MainRepository(photosDatabase,apiService)

    private val _query = MutableLiveData("")
    val query: MutableLiveData<String> = _query

     var photosPagingData:LiveData<PagingData<Photo>>? = null

    fun fetchPhotosLiveData(query: String){
            photosPagingData = photosRepository.getPhotosResultsStream(query)
            .cachedIn(viewModelScope)
        if (query.isNotEmpty())  photosPagingData = photosRepository.getPhotosResultsStream(query)
            .cachedIn(viewModelScope)
    }

    fun updateSearchQuery(query: String){
        _query.value = query
    }
}
@ExperimentalPagingApi
class ListImagesViewModelFactory(private val photosDatabase: PhotosDatabase, private val apiService: ApiService):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListImagesViewModel::class.java)) return ListImagesViewModel(photosDatabase,apiService) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}