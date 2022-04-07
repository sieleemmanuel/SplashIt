package com.buildwithsiele.splashit.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService

@ExperimentalPagingApi
class MainRepository(private val photosDatabase: PhotosDatabase, private val apiService: ApiService) {

     fun getPhotosResultsStream(query: String):LiveData<PagingData<Photo>>{
        val pagingSourceFactory = { photosDatabase.photosDao.getAllPhotos() }
        val pagingConfig = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
             return Pager(
                 config = pagingConfig,
                 remoteMediator = PhotosMediator(photosDatabase,apiService,query),
                 pagingSourceFactory = pagingSourceFactory
             ).liveData
    }

    /*fun getSearchResultsStream(query:String):LiveData<PagingData<Photo>>{
        val pagingSourceFactory = { PagingPhotoSource(apiService, query) }
        val pagingConfig = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }*/

    val photoList = photosDatabase.photosDao.getPhotosList()

    companion object{
        const val PAGE_SIZE = 15
        const val DEFAULT_PAGE_INDEX = 1
    }
}