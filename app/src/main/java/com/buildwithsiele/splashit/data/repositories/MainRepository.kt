package com.buildwithsiele.splashit.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService
import com.buildwithsiele.splashit.data.network.PhotosApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ExperimentalPagingApi
class MainRepository(private val photosDatabase: PhotosDatabase, private val apiService: ApiService) {

    suspend fun updatePhotoList(){
       withContext(Dispatchers.IO){
       val photoList =  PhotosApi.apiService.getPhotos( per_page = PAGE_SIZE
            )
           //for (photo in photoList)
           photosDatabase.photosDao.insertPhotos(photoList)
        }
    }

    fun getPhotosResultsStream():LiveData<PagingData<Photo>>{
        val pagingSourceFactory = { photosDatabase.photosDao.getAllPhotos() }
        val pagingConfig = PagingConfig(PAGE_SIZE,enablePlaceholders = false)
        return Pager(
            config = pagingConfig,
            remoteMediator = PhotosMediator(photosDatabase,apiService),
            pagingSourceFactory = pagingSourceFactory
        ).liveData

    }
    companion object{
        const val PAGE_SIZE = 7
        const val DEFAULT_PAGE_INDEX = 1
    }
}