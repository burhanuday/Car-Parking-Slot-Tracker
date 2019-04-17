package com.burhanuday.carparktracker

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_select_slot.*
import kotlinx.android.synthetic.main.dialog_change_url.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {
    lateinit var gso: GoogleSignInOptions.Builder
    lateinit var sio: GoogleSignInOptions
    lateinit var preference:SharedPreferences
    var restApi:RESTApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        preference = PreferenceManager.getDefaultSharedPreferences(this)!!
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
        sio = gso.build()

        restApi = RESTApi.create(preference.getString("server", Constants.baseUrl))

        val googleSignInClient = GoogleSignIn.getClient(this, sio)
        val account: GoogleSignInAccount? = null
        updateUI(account)

        sign_in_button.setSize(SignInButton.SIZE_WIDE)
        sign_in_button.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 123)
        }

        iv_settings.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_url, null)
            val mBuilder = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setTitle("Change server URL")
            val alertDialog = mBuilder.show()
            dialogView.et_dialog_url.setText(preference!!.getString("server", Constants.baseUrl))
            dialogView.bt_dialog_save.setOnClickListener{
                val URL = dialogView.et_dialog_url.text.toString()
                preference!!.edit().putString("server", URL).apply()
                alertDialog.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIn(task)
        }
    }

    fun handleSignIn(task: Task<GoogleSignInAccount>){
        try {
            val account:GoogleSignInAccount = task.getResult(ApiException::class.java)
            updateUI(account)
        }catch (exc:ApiException){
            exc.printStackTrace()
            Log.i("error", exc.statusCode.toString())
            Toast.makeText(this, exc.statusCode.toString(), Toast.LENGTH_SHORT).show()
            updateUI(null)
        }
    }

    fun updateUI(account:GoogleSignInAccount?){
        if (account!=null){
            val email:String = account.email!!
            val name:String = account.displayName!!
            preference.edit().putString("email", email).commit()
            preference.edit().putString("name", name).commit()
            createNewUser(name, email)
        }
    }

    fun createNewUser(name:String, email:String){
        val call: Call<ResponseBody> = restApi!!.createProfile(name, email)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i("REST FAILURE", t.message)
            }

            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                Log.i("REST SUCCESS", response.message())
                val startMain:Intent = Intent(baseContext, MainActivity::class.java)
                startActivity(startMain)
                finish()
            }
        })
    }
}