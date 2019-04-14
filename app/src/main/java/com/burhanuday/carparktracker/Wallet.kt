package com.burhanuday.carparktracker

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_wallet.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class Wallet : AppCompatActivity() {

    lateinit var preference: SharedPreferences
    var restApi:RESTApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        preference = PreferenceManager.getDefaultSharedPreferences(this)!!
        val email:String = preference.getString("email", null)

        restApi = RESTApi.create(preference.getString("server", Constants.baseUrl))

        getUserData(email)

        btn_recharge_wallet.setOnClickListener{
            var alertDialog:AlertDialog? = null
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Recharge wallet")
            dialogBuilder.setMessage("Add ₹100 to your account?")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                alertDialog!!.dismiss()
                val call:Call<ResponseBody> = restApi!!.rechargeWallet(email, "100")
                call.enqueue(object : Callback<ResponseBody>{
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("REST", t.message)
                        Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        Toast.makeText(baseContext, "Recharged successfully", Toast.LENGTH_SHORT).show()
                        getUserData(email)
                    }
                })
            }
            dialogBuilder.setNegativeButton("Cancel", null)
            alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }

    fun getUserData(email:String){
        val call: Call<UserData> = restApi!!.getUserData(email)
        call.enqueue(object : Callback<UserData> {
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.i("REST", t.message)
            }

            override fun onResponse(call: Call<UserData>, response: retrofit2.Response<UserData>) {
                val data:UserData = response.body()!!
                tv_name.text = data.name
                tv_wallet_amount.text = "₹" + data.wallet.toString()
            }
        })
    }
}
