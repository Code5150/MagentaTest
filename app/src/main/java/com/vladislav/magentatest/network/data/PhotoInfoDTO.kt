package com.vladislav.magentatest.network.data

import com.google.gson.annotations.SerializedName

data class PhotoInfoDTO(
    val id: Int,
    @SerializedName("download_url")
    val downloadUrl: String
)
