package com.burhanuday.carparktracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RESTApi {

    @GET("booking/{mall_name}/spots")
    fun getMall(@Path("mall_name") mallName:String): Call<MallData>
}