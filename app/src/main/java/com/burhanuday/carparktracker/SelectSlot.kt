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
    var mallData:List<MallData>? = null
    var seatList:List<Seat>? = null
    private lateinit var adapter: RecyclerAdapter
    val BASE_URL = "http://5b074d4a.ngrok.io/api/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_slot)
        mallName = intent.getStringExtra("mall_name")

        recycler_view.layoutManager = GridLayoutManager(this, 5)

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val restApi = retrofit.create(RESTApi::class.java)
        val call: Call<MallData> = restApi.getMall(mallName)
        call.enqueue(object : Callback<MallData>{
            override fun onFailure(call: Call<MallData>, t: Throwable) {
                Log.i("REST", t.message)
            }

            override fun onResponse(call: Call<MallData>, response: retrofit2.Response<MallData>) {
                var mall:MallData? = response.body()
                Log.i("REST", mall!!.data!![0].spot!![0].toString())
                seatList = mall!!.data!![0].spot
                //Log.i("REST", mall!!.spot.toString())
                adapter = RecyclerAdapter(seatList)
                recycler_view.adapter = adapter

            }
        })
    }
}
