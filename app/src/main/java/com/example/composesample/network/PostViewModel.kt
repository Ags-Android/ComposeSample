package com.example.composesample.network

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application){
    private val repository = PostRepository()
    private val _posts =  MutableStateFlow<MatchData?>(null)
    val posts: StateFlow<MatchData?> = _posts



    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun fetchMatches() {
        viewModelScope.launch {
            if (NetworkUtils.isInternetAvailable(getApplication())) {
                _posts.value = repository.getPosts()
            } else {
                _isConnected.value = false
            }
        }
    }


}
