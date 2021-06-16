package com.vladislav.magentatest

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.imageLoader
import coil.request.ImageRequest
import coil.util.CoilUtils
import okhttp3.OkHttpClient

class MagentaTestApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Builders.imageRequestBuilder = ImageRequest.Builder(applicationContext)
        Builders.imageLoader = imageLoader
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .build()
    }
}