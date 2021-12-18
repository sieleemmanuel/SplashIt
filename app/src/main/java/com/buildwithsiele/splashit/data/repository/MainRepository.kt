package com.buildwithsiele.splashit.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.buildwithsiele.splashit.data.PagingPhotoSource
import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService
import com.buildwithsiele.splashit.data.network.PhotosApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val photosDatabase: PhotosDao, val apiService: ApiService) {


    suspend fun updatePhotoList(){
       withContext(Dispatchers.IO){
       val photoList =  PhotosApi.apiService.getPhotos( per_page = PAGE_SIZE
            )
           for (photo in photoList)
           photosDatabase.insertPhotos(photo)
        }
    }
    val photos = photosDatabase.getAllPhotos()

    fun getPhotosResultsStream():LiveData<PagingData<Photo>>{
        return Pager(
            config = PagingConfig(PAGE_SIZE,enablePlaceholders = false),
            pagingSourceFactory = {
             PagingPhotoSource(apiService)
            }
        ).liveData

    }
    companion object{
        const val PAGE_SIZE = 50
    }
}