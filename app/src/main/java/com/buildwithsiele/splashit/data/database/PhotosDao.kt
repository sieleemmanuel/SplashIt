package com.buildwithsiele.splashit.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.buildwithsiele.splashit.data.model.Photo

@Dao
interface PhotosDao {
    @Insert
    suspend fun insertPhotos(photo: List<Photo>)

    @Query("SELECT * FROM photos_table")
    fun getAllPhotos(): PagingSource<Int, Photo>

    @Query("DELETE FROM photos_table")
    fun deleteAll()
}