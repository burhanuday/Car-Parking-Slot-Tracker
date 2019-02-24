package com.burhanuday.carparktracker

import android.content.DialogInterface
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.budiyev.android.codescanner.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class CameraActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    var sharedPreferences: SharedPreferences? = null
    var restApi:RESTApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        restApi = RESTApi.create(sharedPreferences!!.getString("server", Constants.baseUrl))
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Confirming slot: ${it.text}", Toast.LENGTH_LONG).show()
                val call: Call<ResponseBody> = restApi!!.verifySlot(sharedPreferences!!.getString("booking_id", "notvalid"), it.text)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("REST", t.message)
                        Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        Toast.makeText(baseContext, response.message(), Toast.LENGTH_SHORT).show()
                        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@CameraActivity)
                        alertDialogBuilder.setPositiveButton("OK", object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                finish()
                            }
                        })
                        alertDialogBuilder.setMessage("Your parking slot has been confirmed successfully")
                        alertDialogBuilder.setTitle("Congratulations")

                        val alertDialog: AlertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    }
                })

            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
