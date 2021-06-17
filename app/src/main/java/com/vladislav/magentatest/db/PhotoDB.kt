package com.vladislav.magentatest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vladislav.magentatest.db.dao.PhotoDAO
import com.vladislav.magentatest.db.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class PhotoDB: RoomDatabase() {
    abstract fun photoDao(): PhotoDAO

    companion object {
        private var INSTANCE: PhotoDB? = null

        fun getPhotoDB(context: Context): PhotoDB? {
            if (INSTANCE == null) {
                synchronized(PhotoDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PhotoDB::class.java,
                        "PhotoDB"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDB() {
            INSTANCE = null
        }
    }
}