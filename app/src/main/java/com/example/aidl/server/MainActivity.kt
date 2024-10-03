package com.example.aidl.server

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.aidl.client.R

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var btnChangeColor: Button
    lateinit var iAIDLColorInterface: IAIDLColorInterface
    var mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iAIDLColorInterface = IAIDLColorInterface.Stub.asInterface(service)
            Log.d(TAG, "onServiceConnected: connected to service")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: disconnected from service")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // bind to the service using the intent with the action "AIDLColorService"
        val intent = Intent("AIDLColorService")
        intent.setPackage("com.example.aidl.server")
        bindService(intent, mConnection, BIND_AUTO_CREATE)

        Log.d(TAG, "onCreate: binding to service / calling the service")

        btnChangeColor = findViewById(R.id.btnChangeColor)
        btnChangeColor.setOnClickListener {
            try {
                val color = iAIDLColorInterface.getColor()
                Log.d(TAG, "onCreate: color from service: $color")
                it.setBackgroundColor(color)
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: error getting color from service: ${e.message}")
            }
        }

    }

}