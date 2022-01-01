package com.buildwithsiele.splashit.data.database

import android.content.Context
import androidx.room.*
import com.buildwithsiele.splashit.data.model.Converter
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.model.RemoteKeys

@Database(entities = [Photo::class, RemoteKeys::class], version = 1,exportSchema = false)
@TypeConverters(Converter::class)
abstract class PhotosDatabase:RoomDatabase() {
    abstract val photosDao:PhotosDao
    abstract val repoKeysDao:RemoteKeysDao

    companion object{
        @Volatile
        private var INSTANCE:PhotosDatabase? = null

        fun getInstance(context: Context):PhotosDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PhotosDatabase::class.java,
                        "unsplash_photos_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance

            }
                return instance
            }

        }
    }

}