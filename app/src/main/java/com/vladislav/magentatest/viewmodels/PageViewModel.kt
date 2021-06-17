package com.vladislav.magentatest.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.repository.ImageRepository
import com.vladislav.magentatest.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PageViewModel : ViewModel() {

    private val TAG = "PageViewModel"

    private var _pld: MutableLiveData<PagingData<Photo>>
    var pld: LiveData<PagingData<Photo>>
        private set

    private val _likedPictures = liveData(Dispatchers.IO) {
        emit(ImageRepository.loadSavedImages())
    } as MutableLiveData
    val likedPictures = _likedPictures as LiveData<List<Int>>

    init {
        _pld = ImageRepository.imagesLiveData()
            .map { data -> data.map { p -> likedPictures.value?.let {
                Photo(p).apply {
                    if(id in it) this.liked = true }
            } ?: Photo(p)} }
            .cachedIn(viewModelScope) as MutableLiveData
        pld = toLiveData(_pld)
    }

    private fun writeImage(bmp: Bitmap, id: Int, dir: File) {
        val imgFile = File(dir, "$id${MainActivity.JPG}")
        with(FileOutputStream(imgFile)) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, this)
            Log.d(TAG, "image written")
            this.close()
        }
    }

    private fun deleteFile(file: File, context: Context) {
        file.delete()
        if (file.exists()) {
            context.deleteFile(file.name)
        }
        Log.d(TAG, "file deleted")
    }

    private fun insertIntoDB(photo: Photo) {
        ImageRepository.insertPhoto(photo)
        Log.d(TAG, "image info inserted")
    }

    private fun deleteFromDB(id: Int) {
        ImageRepository.deletePhotoById(id)
        Log.d(TAG, "image info deleted")
    }

    fun likeImage(bmp: Bitmap, photo: Photo, dir: File) {
        viewModelScope.launch(Dispatchers.IO) {
            writeImage(bmp, photo.id, dir)
            insertIntoDB(photo)
            withContext(Dispatchers.Main) {
                _likedPictures.value = _likedPictures.value?.plus(photo.id)
            }
            Log.d(TAG, _likedPictures.value!!.joinToString())
        }
    }

    fun removeLike(id: Int, file: File, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFromDB(id)
            deleteFile(file, context)
            /*_pictures.value!!.find { it.id == id }.apply {
                this?.liked = false
            }*/
            withContext(Dispatchers.Main) {
                _pld.value = pld.value?.map { data -> when(data.id == id) {
                    true -> data.apply { this.liked = false }
                    false -> data
                } }
                _likedPictures.value = _likedPictures.value!!.filter { it != id }
            }
            Log.d(TAG, _likedPictures.value!!.joinToString())
        }
    }

    private inline fun<T> toLiveData(mld: MutableLiveData<T>) = mld as LiveData<T>
}

