package com.example.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        val requestCode = 1
        var id = 0

        val notification = NotificationCompat.Builder(this,
                getString(R.string.channelID))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText("placeholder")
                .setAutoCancel(true)
                .build()

        val notificationManager = NotificationManagerCompat.from(this)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                notificationManager.notify(id++, notification)
            }
        }
        registerReceiver(receiver,
                IntentFilter("NEW_PRODUCT_ADDED_BROADCAST"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val notificationChannel = NotificationChannel(
                getString(R.string.channelID),
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.description =
                getString(R.string.channel_description)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}