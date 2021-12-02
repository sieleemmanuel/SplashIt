package com.buildwithsiele.splashit.data.repository

import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.network.PhotosApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val photosDatabase: PhotosDao) {
    suspend fun updatePhotoList(per_page:Int = 30){
       withContext(Dispatchers.IO){
       val photoList =  PhotosApi.apiService.getPhotos(
                per_page,
                "Y7Eo8c546fG_BeKByFyoYucQhSjAjDkz1kM3YZHVrnE"
            )
           for (photo in photoList)
           photosDatabase.insertPhotos(photo)
        }
    }
    val photos = photosDatabase.getAllPhotos()
}