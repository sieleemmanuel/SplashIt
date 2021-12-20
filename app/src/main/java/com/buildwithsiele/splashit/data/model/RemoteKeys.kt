package com.buildwithsiele.splashit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_table")
data class RemoteKeys(
    @PrimaryKey val repoId:String,
    @ColumnInfo(name = "prevKey")val prevKey: Int?,
    @ColumnInfo(name = "nextKey")val nextKey: Int?
)
