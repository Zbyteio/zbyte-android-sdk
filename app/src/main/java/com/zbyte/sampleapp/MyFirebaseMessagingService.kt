package com.zbyte.sampleapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Class to handle the Push Notifications Received
 *
 * @since 17/06/2022
 * @author Yash Parikh
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Function to handle the push notification view when received
     * setting the parameters to the notification
     *
     * @param message RemoteMessage received from the Firebase
     */
    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            showNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    /**
     * Function to handle the new token for FCM received
     *
     * @param token String Token that will be updated for the devices
     */
    override fun onNewToken(token: String) {
        Log.e("Token Service::", token)
    }

    /**
     * Function to set the parameters and customize the received notification
     *
     * @param title Notification Title from RemoteMessage
     * @param message Notification Message/Details from RemoteMessage
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, ZByteActivity::class.java)
        val channelId = getString(R.string.app_name)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    channelId, "zByte",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager.notify(0, builder.build())
    }
}