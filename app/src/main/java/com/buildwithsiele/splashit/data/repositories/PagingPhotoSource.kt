package com.buildwithsiele.splashit.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.ApiService
import retrofit2.HttpException

class PagingPhotoSource(private val apiService: ApiService,  private val query:String): PagingSource<Int,Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
       val page = params.key?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.search(page = page, query = query).results
            LoadResult.Page(
                response,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page -1,
                nextKey = if (response.isEmpty()) null else page +1
            )
        }catch (exception:Exception){
            LoadResult.Error(exception)

        }
        catch (httpException:HttpException){
            LoadResult.Error(httpException)
        }
    }
    companion object{
        private const val DEFAULT_PAGE_INDEX = 1
    }
}