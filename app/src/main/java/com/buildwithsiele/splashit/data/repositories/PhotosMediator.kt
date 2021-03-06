package com.buildwithsiele.splashit.data.repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.model.RemoteKeys
import com.buildwithsiele.splashit.data.network.ApiService
import com.buildwithsiele.splashit.data.repositories.MainRepository.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException


@ExperimentalPagingApi
class PhotosMediator(
    private val photosDatabase: PhotosDatabase,
    private val apiService: ApiService,
    private val query:String
) : RemoteMediator<Int, Photo>() {
    private val photosDao = photosDatabase.photosDao
    private val remoteKeysDao = photosDatabase.repoKeysDao
    private lateinit var response: List<Photo>

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>): MediatorResult {

        val page = when (val pagedKeyData = getKeyPagedData(loadType, state)) {
            is MediatorResult.Success -> {
                return pagedKeyData
            }
            else -> pagedKeyData!! as Int
        }
        return try {
            response = if (query.isEmpty()) {
                apiService.getPhotos(page = page)
            }else {
                apiService.search(query = query, page = page).results
            }
            val isEndOfList = response.isEmpty()
            photosDatabase.withTransaction {
                //clear all the tables in database
            if (loadType == LoadType.REFRESH) {
                photosDao.deleteAll()
                remoteKeysDao.clearRemoteKeys()
            }
            val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
            val nextKey = if (isEndOfList) null else page + 1
            val keys = response.map {photo ->
                RemoteKeys(repoId = photo.id, prevKey = prevKey, nextKey = nextKey)
            }
            remoteKeysDao.insertAll(keys)
            photosDao.insertPhotos(response)
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (exception: Exception) {
           MediatorResult.Error(exception)

        } catch (httpException: HttpException) {
           MediatorResult.Error(httpException)
        }
    }

    private suspend fun getKeyPagedData(loadType: LoadType, state: PagingState<Int, Photo>):Any?{
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getClosestRemoteKey(state)
                remoteKey?.nextKey?.minus(1)?:DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
               if (remoteKey?.nextKey==null)
                   return MediatorResult.Success(endOfPaginationReached = true)
                remoteKey.nextKey
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Photo>): RemoteKeys? {
        return state.lastItemOrNull()
            ?.let { photo ->
                remoteKeysDao.getRemoteKeysPhotoID(photo.id)
            }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Photo>): RemoteKeys? {
        return state.anchorPosition
            ?.let { position ->
                state.closestItemToPosition(position)?.let {repoId ->
                    remoteKeysDao.getRemoteKeysPhotoID(repoId.id)
                }
            }
    }

}