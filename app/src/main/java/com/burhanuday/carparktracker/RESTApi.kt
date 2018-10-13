package com.burhanuday.carparktracker

import retrofit2.Call
import retrofit2.http.GET

interface RESTApi {

    @GET("5bae32d52e00006400bb41a1")
    fun getData(): Call<List<Seat>>
}