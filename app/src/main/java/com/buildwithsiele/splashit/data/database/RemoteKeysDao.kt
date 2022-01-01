package com.buildwithsiele.splashit.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buildwithsiele.splashit.data.model.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys_table WHERE repoId =:id")
    suspend fun getRemoteKeysPhotoID(id:String): RemoteKeys?

    @Query("DELETE FROM remote_keys_table")
    suspend fun clearRemoteKeys()
}