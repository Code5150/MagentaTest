package com.vladislav.magentatest

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import com.vladislav.magentatest.repository.ImageRepository
import okhttp3.OkHttpClient

class MagentaTestApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        ImageRepository.initDatabase(applicationContext)
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