package com.vladislav.magentatest.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.db.PhotoDB
import com.vladislav.magentatest.db.PhotoEntity
import com.vladislav.magentatest.network.ApiInterface
import com.vladislav.magentatest.ui.fragments.PlaceholderFragment
import com.vladislav.magentatest.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

typealias PhotoList = List<Photo>

//TODO: список лайкнутых отдельно
class PageViewModel : ViewModel() {

    private val TAG = "PageViewModel"

    private val apiService = ApiInterface()

    private lateinit var database: PhotoDB

    fun initDatabase(context: Context) {
        database = PhotoDB.getPhotoDB(context)!!
    }

    private var page = 1

    private val _pictures = liveData(Dispatchers.IO) {
        //TODO: Проверка на существование записей в бд и соотв. отметка их как лайкнутые
        var existingImages: List<Int>
        withContext(Dispatchers.IO) {
            existingImages = database.photoDao().selectAllIds()
        }
        val loadedImages = apiService.getImagesPage(page).map { Photo(it) }
        loadedImages.forEach { if (it.id in existingImages) it.liked = true }
        emit(loadedImages)
    } as MutableLiveData
    public val pictures = _pictures as LiveData<PhotoList>

    private val _likedPictures = liveData(Dispatchers.IO){
        emit(database.photoDao().selectAllIds())
    } as MutableLiveData
    public val likedPictures = _likedPictures as LiveData<List<Int>>

    fun loadMore() {
        page++
        viewModelScope.launch(Dispatchers.IO) {
            _pictures.value = _pictures.value?.plus(
                apiService.getImagesPage(page).map { Photo(it) }
            )
        }
    }

    fun writeImage(bmp: Bitmap, id: Int, dir: File) = viewModelScope.launch(Dispatchers.IO) {
        val imgFile = File(dir, "$id${MainActivity.JPG}")
        with(FileOutputStream(imgFile)){
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, this)
            Log.d(TAG, "image written")
        }
    }

    fun deleteFile(file: File, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            file.delete()
            if(file.exists()){
               context.deleteFile(file.name)
            }
            Log.d(TAG, "file deleted")
        }
    }

    fun insertIntoDB(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            database.photoDao().insert(PhotoEntity(photo.id, photo.width, photo.height))
            Log.d(TAG, "image info inserted")
        }
    }

    fun deleteFromDB(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            database.photoDao().deleteById(id)
            Log.d(TAG, "image info deleted")
        }
    }
}

