package com.burhanuday.carparktracker

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var gso: GoogleSignInOptions.Builder
    lateinit var sio: GoogleSignInOptions
    lateinit var preference:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        preference = PreferenceManager.getDefaultSharedPreferences(this)!!
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
        sio = gso.build()

        val googleSignInClient = GoogleSignIn.getClient(this, sio)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)

        sign_in_button.setSize(SignInButton.SIZE_WIDE)
        sign_in_button.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 123)
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
            preference.edit().putString("email", email).apply()
            val startMain:Intent = Intent(this, MainActivity::class.java)
            startActivity(startMain)
        }
    }
}
