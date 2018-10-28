package com.burhanuday.carparktracker

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_change_url.view.*

class MainActivity : AppCompatActivity() {

    var sharedPreferences:SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar!!.title = "Car Park Tracker"
        //actionBar.subtitle = ""
        actionBar.elevation = 4.0F
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)

        fl_mall_1.setOnClickListener {
            val intent = Intent(this, SelectSlot::class.java)
            intent.putExtra("mall_name", "mall1")
            startActivity(intent)
        }

        fl_mall_2.setOnClickListener{
            val intent = Intent(this, SelectSlot::class.java)
            intent.putExtra("mall_name", "mall2")
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_change_url -> {
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_url, null)
                val mBuilder = AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setTitle("Change server URL")
                val alertDialog = mBuilder.show()
                dialogView.et_dialog_url.setText(sharedPreferences!!.getString("server", "http://c965e998.ngrok.io/api/v1/"))
                dialogView.bt_dialog_save.setOnClickListener{
                    val URL = dialogView.et_dialog_url.text.toString()
                    sharedPreferences!!.edit().putString("server", URL).apply()
                    alertDialog.dismiss()
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
