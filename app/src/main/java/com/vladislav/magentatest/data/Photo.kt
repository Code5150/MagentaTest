package com.vladislav.magentatest.data

import coil.memory.MemoryCache
import com.vladislav.magentatest.db.entities.PhotoEntity
import com.vladislav.magentatest.network.data.PhotoInfoDTO

data class Photo(
    val id: Int,
    val width: Int,
    val height: Int,
    var cacheKey: MemoryCache.Key? = null,
    var liked: Boolean = false
) {
    constructor(dto: PhotoInfoDTO) : this(dto.id, dto.width, dto.height)
    constructor(entity: PhotoEntity): this(entity.id, entity.width, entity.height)
}
