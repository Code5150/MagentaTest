package com.vladislav.magentatest.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.datasource.ImagesPagingSource
import com.vladislav.magentatest.db.PhotoDB
import com.vladislav.magentatest.db.entities.PhotoEntity

object ImageRepository {
    private lateinit var database: PhotoDB

    fun initDatabase(context: Context) {
        database = PhotoDB.getPhotoDB(context)!!
    }

    fun loadSavedImages() = database.photoDao().selectAllIds()

    fun insertPhoto(photo: Photo) {
        database.photoDao().insert(PhotoEntity(photo.id, photo.width, photo.height))
    }

    fun deletePhotoById(id: Int) {
        database.photoDao().deleteById(id)
    }

    fun imagesLiveData() =
        Pager(PagingConfig(pageSize = 30)) { ImagesPagingSource() }.liveData
}