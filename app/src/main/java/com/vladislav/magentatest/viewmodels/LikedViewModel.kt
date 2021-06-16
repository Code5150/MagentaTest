package com.vladislav.magentatest.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.vladislav.magentatest.db.PhotoDB

class LikedViewModel: ViewModel() {
    private lateinit var database: PhotoDB

    fun initDatabase(context: Context) {
        database = PhotoDB.getPhotoDB(context)!!
    }
}