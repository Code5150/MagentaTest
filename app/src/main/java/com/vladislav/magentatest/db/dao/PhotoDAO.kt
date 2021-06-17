package com.vladislav.magentatest.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vladislav.magentatest.db.entities.PhotoEntity

@Dao
interface PhotoDAO {
    @Query("SELECT * FROM photo")
    fun selectAll(): List<PhotoEntity>

    @Query("SELECT id FROM photo")
    fun selectAllIds(): List<Int>

    @Insert
    fun insert(photo: PhotoEntity)

    @Delete
    fun delete(photo: PhotoEntity)

    @Query("DELETE FROM photo WHERE photo.id = :id")
    fun deleteById(id: Int)
}