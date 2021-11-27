package com.buildwithsiele.splashit.ui.listimages

import android.util.Log
import androidx.lifecycle.*
import com.buildwithsiele.splashit.data.model.Image
import com.buildwithsiele.splashit.data.network.PhotosApi
import kotlinx.coroutines.launch

class ListImagesViewModel: ViewModel() {

    init {
        getAllPhotos()
    }
    private val _photos = MutableLiveData<List<Image>>()
    val photos:LiveData<List<Image>>
    get() = _photos
    


    private fun getAllPhotos(per_page:Int = 30,){
        viewModelScope.launch {
            try {
                val photoList = PhotosApi.apiService.getPhotos(
                    per_page,
                    "Y7Eo8c546fG_BeKByFyoYucQhSjAjDkz1kM3YZHVrnE"
                )
                _photos.value = photoList
            }catch (e:Exception){
                _photos.value = ArrayList()
                Log.d("ListImagesViewModel", "Error: ${e.message}")
            }
        }
    }
}
class ListImagesViewModelFactory:ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListImagesViewModel::class.java)) return ListImagesViewModel() as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}