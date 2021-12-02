package com.buildwithsiele.splashit.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.repository.MainRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PhotoDetailsViewModel(private val photosDatabase:PhotosDao) : ViewModel() {
    init {
        viewModelScope.launch {
            try {
                photosRepository.updatePhotoList()
            }catch (e:Exception){
                Log.d("ViewModel", "Error: ${e.message} ")
            }

        }
    }
    //reference to repository
    private val photosRepository = MainRepository(photosDatabase)
    val photoList = photosRepository.photos



/* @SuppressLint("UseCompatLoadingForDrawables")
 v imageList = mutableListOf(
     R.drawable.splash_background,
     R.drawable.ic_launcher_background,
     R.drawable.ic_launcher_foreground,
     R.drawable.ic_search_black_24dp
 )*/
}
class PhotoDetailsViewModelFactory(val photosDatabase: PhotosDao):ViewModelProvider.Factory{
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
     if(modelClass.isAssignableFrom(PhotoDetailsViewModel::class.java)){
         return PhotoDetailsViewModel(photosDatabase) as T
     }
     throw IllegalArgumentException("Unknown ViewModel Class")
 }

}