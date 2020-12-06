package com.example.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


const val PRODUCT_NAME = "name"

class MainActivity : AppCompatActivity() {
    lateinit var receiver: BroadcastReceiver
    private val NEW_PRODUCT_ADDED_BROADCAST = "SHOPPING_LIST_APP.NEW_PRODUCT_ADDED_BROADCAST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        var id = 0

        val intent = Intent()
        intent.component = ComponentName("com.example.mini_project", "com.example.mini_project..productList.ProductListActivity")

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(this,
                getString(R.string.channelID))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_title))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val notification = notificationBuilder
                        .setContentText(intent?.getStringExtra(PRODUCT_NAME))
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