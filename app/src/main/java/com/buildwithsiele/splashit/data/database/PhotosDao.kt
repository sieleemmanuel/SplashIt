package com.buildwithsiele.splashit.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.buildwithsiele.splashit.data.model.Photo

@Dao
interface PhotosDao {
    @Insert
    suspend fun insertPhotos(photo: Photo)

    @Query("SELECT * FROM photos_table")
    fun getAllPhotos():LiveData<List<Photo>>
}