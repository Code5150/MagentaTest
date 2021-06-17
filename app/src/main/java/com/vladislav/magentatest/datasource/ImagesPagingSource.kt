package com.vladislav.magentatest.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vladislav.magentatest.network.ApiService
import com.vladislav.magentatest.network.data.PhotoInfoDTO
import retrofit2.HttpException
import java.io.IOException

class ImagesPagingSource: PagingSource<Int, PhotoInfoDTO>() {
    private val apiService = ApiService()

    companion object {
        private const val INITIAL_KEY: Int = 1
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoInfoDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoInfoDTO> {
        val position = params.key ?: INITIAL_KEY
        try {
            return LoadResult.Page(
                data = apiService.getImagesPage(position),
                prevKey = null,
                nextKey = position + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }

    }
}