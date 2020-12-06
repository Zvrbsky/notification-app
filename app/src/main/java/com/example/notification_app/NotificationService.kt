package com.example.notification_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val PRODUCT_NAME = "name"
const val PRODUCT_ID = "product id"
const val NEW_PRODUCT_ADDED_BROADCAST = "SHOPPING_LIST_APP.NEW_PRODUCT_ADDED_BROADCAST"
const val SHOW_PRODUCT_DETAILS_BROADCAST = "NOTIFICATION_APP.SHOW_PRODUCT_DETAILS_BROADCAST"

class NotificationService : Service() {
    lateinit var receiver: BroadcastReceiver

    override fun onCreate() {
        createNotificationChannel()
        var id = 0

        val notificationBuilder = NotificationCompat.Builder(this,
            getString(R.string.channelID))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.notification_title))
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val reactionIntent = Intent(SHOW_PRODUCT_DETAILS_BROADCAST)
                    val productId =  intent?.getLongExtra(PRODUCT_ID, 0)
                    Log.d("MainActivity", "Broadcast received for product with id: $productId")
                    reactionIntent.putExtra(PRODUCT_ID, productId)
                    val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, reactionIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                    val notification = notificationBuilder
                        .setContentText(intent?.getStringExtra(PRODUCT_NAME))
                        .addAction(R.mipmap.ic_launcher, "Edit", pendingIntent)
                        .build()
                    notificationManager.notify(id++, notification)
                }
            }
        }

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Notifications turned on", Toast.LENGTH_SHORT).show()


        registerReceiver(receiver, IntentFilter(NEW_PRODUCT_ADDED_BROADCAST))
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Notifications turned off", Toast.LENGTH_SHORT).show()
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