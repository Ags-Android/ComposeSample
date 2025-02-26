package com.example.composesample.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PostRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getPosts(): MatchData? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getPosts().execute()
            if (response.isSuccessful) response.body() else null
        }
    }
}