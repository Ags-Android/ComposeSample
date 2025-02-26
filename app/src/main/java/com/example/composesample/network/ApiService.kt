package com.example.composesample.network

import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("sapk01222019186652.json")
    fun getPosts(): Call<MatchData>
}