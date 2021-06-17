package com.vladislav.magentatest.other

import androidx.recyclerview.widget.DiffUtil
import com.vladislav.magentatest.data.Photo

typealias PhotoCallback = DiffUtil.ItemCallback<Photo>

object PhotoComparator: PhotoCallback() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.width == newItem.width
                && oldItem.height == newItem.height
                && oldItem.liked == newItem.liked
    }
}