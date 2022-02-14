package com.buildwithsiele.splashit.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService

@ExperimentalPagingApi
class MainRepository(private val photosDatabase: PhotosDatabase, private val apiService: ApiService) {

     fun getPhotosResultsStream():LiveData<PagingData<Photo>>{
        val pagingSourceFactory = { photosDatabase.photosDao.getAllPhotos() }
        val pagingConfig = PagingConfig(PAGE_SIZE, enablePlaceholders = true)
        return Pager(
            config = pagingConfig,
            remoteMediator = PhotosMediator(photosDatabase,apiService),
            pagingSourceFactory = pagingSourceFactory
        ).liveData

    }
    suspend fun localStorageExist():Boolean = photosDatabase.photosDao.localStorageExist()

    val photoList = photosDatabase.photosDao.getPhotosList()

    companion object{
        const val PAGE_SIZE = 3
        const val DEFAULT_PAGE_INDEX = 1
    }
}