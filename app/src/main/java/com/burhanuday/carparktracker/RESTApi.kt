package com.burhanuday.carparktracker

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


interface RESTApi {

    @GET("booking/{mall_name}/spots")
    fun getMall(@Path("mall_name") mallName:String): Call<MallData>

    @POST("booking/book")
    fun updateSlot(@Body seat: Seat): Call<ResponseBody>

    @POST("booking/verify/{email}/{booking_id}/{code}")
    fun verifySlot(@Path("email") email:String, @Path("booking_id") bookingId:String, @Path("code") code:String): Call<ResponseBody>

    @POST("booking/user/{name}/{email}")
    fun createProfile(@Path("name") name:String, @Path("email") email:String): Call<ResponseBody>

    @GET("booking/user/{email}")
    fun getUserData(@Path("email") email: String): Call<UserData>

    @POST("booking/recharge/{email}/{recharge_amount}")
    fun rechargeWallet(@Path("email") email: String, @Path("recharge_amount") recharge_amount:String): Call<ResponseBody>


    companion object {
        fun create(baseURL:String): RESTApi{

            val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()

            val retrofit = Retrofit.Builder().baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

            return retrofit.create(RESTApi::class.java)
        }
    }

}