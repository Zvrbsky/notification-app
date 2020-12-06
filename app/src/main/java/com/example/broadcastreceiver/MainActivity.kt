package com.example.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


const val PRODUCT_NAME = "name"
const val PRODUCT_ID = "product id"
const val NEW_PRODUCT_ADDED_BROADCAST = "SHOPPING_LIST_APP.NEW_PRODUCT_ADDED_BROADCAST"
const val SHOW_PRODUCT_DETAILS_BROADCAST = "BROADCAST_RECEIVER.SHOW_PRODUCT_DETAILS_BROADCAST"

class MainActivity : AppCompatActivity() {
    lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        var id = 0

        val notificationBuilder = NotificationCompat.Builder(this,
                getString(R.string.channelID))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_title))
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val reactionIntent = Intent(SHOW_PRODUCT_DETAILS_BROADCAST)
                val productId =  intent?.getLongExtra(PRODUCT_ID, 0)
                Log.d("MainActivity", "Broadcast received for product with id: $productId")
                reactionIntent.putExtra(PRODUCT_ID, productId)
                val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, reactionIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val notification = notificationBuilder
                        .setContentText(intent?.getStringExtra(PRODUCT_NAME))
                        .setContentIntent(pendingIntent)
                        .build()
                notificationManager.notify(id++, notification)
            }
        }
        registerReceiver(receiver,
                IntentFilter(NEW_PRODUCT_ADDED_BROADCAST))
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