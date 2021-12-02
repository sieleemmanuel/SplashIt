package com.buildwithsiele.splashit.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.buildwithsiele.splashit.data.database.PhotosDao
import com.buildwithsiele.splashit.data.repository.MainRepository
import kotlinx.coroutines.launch

class ListImagesViewModel(photosDatabase: PhotosDao): ViewModel() {
    //reference to repository
    private val photosRepository = MainRepository(photosDatabase)

    init {
        updateDataFromRepository()
    }
    val photos = photosRepository.photos

    private fun updateDataFromRepository(){
        viewModelScope.launch {
            try {
                photosRepository.updatePhotoList()
            }catch (e:Exception){
                Log.d("ViewModel", "Error: ${e.message} ")
            }

        }
    }
}
class ListImagesViewModelFactory(private val photosDatabase: PhotosDao):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListImagesViewModel::class.java)) {
            return ListImagesViewModel(photosDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}