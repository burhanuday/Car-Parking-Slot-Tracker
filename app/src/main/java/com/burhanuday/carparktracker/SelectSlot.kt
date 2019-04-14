package com.burhanuday.carparktracker

import android.Manifest
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_select_slot.*
import kotlinx.android.synthetic.main.dialog_change_url.view.*
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectSlot : AppCompatActivity() {

    lateinit var mallName:String
    var mallData:List<MallData>? = null
    var seatList:List<Seat>? = null
    private lateinit var adapter: RecyclerAdapter
    var sharedPreferences:SharedPreferences? = null
    var restApi:RESTApi? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_slot)
        RecyclerAdapter.lastCheckedPos = -1
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        restApi = RESTApi.create(sharedPreferences!!.getString("server", Constants.baseUrl))
        mallName = intent.getStringExtra("mall_name")
        recycler_view.layoutManager = GridLayoutManager(this, 5)
        retrieveData()

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
                val tempSeat = seatList!![toBook]
                tempSeat.isBooked = true
                tempSeat.email = sharedPreferences!!.getString("email", "no email")
                tempSeat.mall = mallName
                val call:Call<ResponseBody> = restApi!!.updateSlot(tempSeat)
                call.enqueue(object : Callback<ResponseBody>{
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("REST", t.message)
                        Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        Toast.makeText(baseContext, "Booked successfully", Toast.LENGTH_SHORT).show()
                        val seat = response.body()!!.string()
                        try {
                            var obj: JSONObject = JSONObject(seat)
                            val booking_id = obj.getString("booking_id")
                            Log.i("REST booking id", booking_id)
                            sharedPreferences!!.edit().putString("booking_id", booking_id).commit()
                        } catch (t: Throwable) {
                            Log.i("REST error", "Could not parse malformed JSON: \"$seat\"")
                        }
                        Log.i("response", seat)
                        retrieveData()
                    }
                })
            }
            dialogBuilder.setNegativeButton("Cancel", null)
            alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }

    fun retrieveData(){
        val call: Call<MallData> = restApi!!.getMall(mallName)
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
    }


}
