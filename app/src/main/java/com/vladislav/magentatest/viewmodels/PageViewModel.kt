package com.vladislav.magentatest.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.vladislav.magentatest.network.ApiInterface
import com.vladislav.magentatest.network.data.PhotoInfoDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias PhotoList = List<PhotoInfoDTO>

class PageViewModel : ViewModel() {

    private val apiService = ApiInterface()

    private var page = 1

    private val _pictures = liveData {
        emit(apiService.getImagesPage(page))
    } as MutableLiveData
    public val pictures = _pictures as LiveData<PhotoList>

    suspend fun loadMore() {
        page++
        viewModelScope.launch(Dispatchers.IO) {
            _pictures.value = _pictures.value?.plus(apiService.getImagesPage(page))
        }
    }
}

