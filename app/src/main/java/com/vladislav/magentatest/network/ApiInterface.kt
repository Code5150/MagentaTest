package com.vladislav.magentatest.network

import com.vladislav.magentatest.network.data.PhotoInfoDTO
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.properties.Delegates

interface ApiInterface {

    @GET("v2/list")
    suspend fun getImagesPage(@Query("page") page: Int): List<PhotoInfoDTO>

    companion object {

        const val BASE_URL = "https://picsum.photos/"
        const val ID = "id/"

        private val interfaceInstance = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        operator fun invoke(): ApiInterface = interfaceInstance
    }
}