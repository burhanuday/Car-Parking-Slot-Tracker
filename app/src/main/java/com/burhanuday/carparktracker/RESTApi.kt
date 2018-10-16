package com.burhanuday.carparktracker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RESTApi {

    @GET("booking/{mall_name}/spots")
    fun getMall(@Path("mall_name") mallName:String): Call<MallData>

    @POST("booking/book")
    fun updateSlot(@Body seat: Seat): Call<Seat>

    /*
    companion object {
        fun create(): RESTApi{
            val retrofit = Retrofit.Builder().baseUrl("http://5b074d4a.ngrok.io/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(RESTApi::class.java)
        }
    }
    */
}