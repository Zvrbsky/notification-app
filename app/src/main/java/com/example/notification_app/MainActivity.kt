package com.example.notification_app


import android.content.*
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun turnOnNotificationOnClick(view: View) {
        Intent(this, NotificationService::class.java).also { intent ->
            startService(intent)
        }
    }

    fun turnOffNotificationOnClick(view: View) {
        Intent(this, NotificationService::class.java).also { intent ->
            stopService(intent)
        }
    }
}