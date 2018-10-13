package com.burhanuday.carparktracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_select_slot.*
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectSlot : AppCompatActivity() {

    lateinit var mallName:String
    var seatList:List<Seat>? = null
    private lateinit var adapter: RecyclerAdapter
    val BASE_URL = "http://www.mocky.io/v2/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_slot)
        mallName = intent.getStringExtra("mall_name")

        recycler_view.layoutManager = GridLayoutManager(this, 5)


        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val restApi = retrofit.create(RESTApi::class.java)
        val call: Call<List<Seat>> = restApi.getData()
        call.enqueue(object : Callback<List<Seat>>{
            override fun onFailure(call: Call<List<Seat>>, t: Throwable) {
                Log.i("ERROR", t.message)
            }

            override fun onResponse(call: Call<List<Seat>>, response: retrofit2.Response<List<Seat>>) {
                seatList = response.body()
                adapter = RecyclerAdapter(seatList)
                recycler_view.adapter = adapter
            }
        })
    }
}
