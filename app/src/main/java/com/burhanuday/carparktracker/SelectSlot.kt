package com.burhanuday.carparktracker

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
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

    /*
    private val restApi by lazy {
        RESTApi.create()
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_slot)
        mallName = intent.getStringExtra("mall_name")
        recycler_view.layoutManager = GridLayoutManager(this, 5)

        val retrofit = Retrofit.Builder().baseUrl("http://5ecb6243.ngrok.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val restApi =  retrofit.create(RESTApi::class.java)

        val call: Call<MallData> = restApi.getMall(mallName)
        call.enqueue(object : Callback<MallData>{
            override fun onFailure(call: Call<MallData>, t: Throwable) {
                Log.i("REST", t.message)
            }

            override fun onResponse(call: Call<MallData>, response: retrofit2.Response<MallData>) {
                val mall:MallData? = response.body()
                seatList = mall!!.data!![0].spot
                adapter = RecyclerAdapter(seatList)
                recycler_view.adapter = adapter
            }
        })

        bt_book_slot.setOnClickListener {
            var alertDialog:AlertDialog? = null
            val toBook:Int = RecyclerAdapter.lastCheckedPos
            if (toBook == -1){
                Toast.makeText(baseContext, "Select a slot to book", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Confirmation dialog")
            dialogBuilder.setMessage("Are you sure your want to book slot P${toBook+1}?")
            dialogBuilder.setPositiveButton("Book seat") { dialog, which ->
                alertDialog!!.dismiss()
                seatList!![toBook].isBooked = true
                seatList!![toBook].email = "burhanuday"
                seatList!![toBook].mall = mallName
                val call:Call<Seat> = restApi.updateSlot(seatList!![toBook])
                call.enqueue(object : Callback<Seat>{
                    override fun onFailure(call: Call<Seat>, t: Throwable) {
                        Log.i("REST", t.message)
                    }

                    override fun onResponse(call: Call<Seat>, response: retrofit2.Response<Seat>) {
                        Toast.makeText(baseContext, "Booked successfully", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            dialogBuilder.setNegativeButton("Cancel", null)
            alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }
}
