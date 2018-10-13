package com.burhanuday.carparktracker

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fl_mall_1.setOnClickListener {
            val intent = Intent(this, SelectSlot::class.java)
            intent.putExtra("mall_name", "aeon")
            startActivity(intent)
        }

        fl_mall_2.setOnClickListener{
            val intent = Intent(this, SelectSlot::class.java)
            intent.putExtra("mall_name", "arabia")
            startActivity(intent)
        }
    }
}
