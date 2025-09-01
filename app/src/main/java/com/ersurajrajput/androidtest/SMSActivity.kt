package com.ersurajrajput.androidtest

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ersurajrajput.androidtest.Models.SMSModel
import com.ersurajrajput.androidtest.databinding.ActivitySmsactivityBinding
import java.security.Permissions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SMSActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsactivityBinding
    private  var SMS_PERMISSION_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsactivityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        chekAndReqPermission()



    }
    fun readSMS() {
        val smsList = mutableListOf<SMSModel>()
        val cursor = contentResolver.query(
            Uri.parse("content://sms/inbox"),
            arrayOf("address","date","body"),
            null,
            null,
            "date DESC"
        )

        cursor.use {
            val indexAddress = it?.getColumnIndex("address")
            val indexBody = it?.getColumnIndex("body")
            val indexDate = it?.getColumnIndex("date")
            while (it?.moveToNext() ?: false){
                val sender = it.getString(indexAddress?:0)
                val message = it.getString(indexBody?:0)

                val timestamp = it.getLong(indexDate?:0)
                val date = Date(timestamp)
                val formate = SimpleDateFormat("dd/mm/yyyy hh:mm:ss", Locale.getDefault())
                val formatedDate = formate.format(date)
                smsList.add(SMSModel(
                    id = "i",
                    sender = sender,
                    message = message,
                    date = formatedDate.toString(),
                ))
                Log.d("MyTag","$formatedDate.")

            }
        }
        Toast.makeText(this,smsList[0].toString(), Toast.LENGTH_SHORT).show()
    }
    public fun chekAndReqPermission(){
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_SMS),SMS_PERMISSION_CODE)


        }else{
            readSMS()
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

    }

}