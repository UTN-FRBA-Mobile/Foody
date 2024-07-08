package ar.edu.utn.frba.foody.ui.dataBase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import ar.edu.utn.frba.foody.MainComposeActivity
import ar.edu.utn.frba.foody.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context


class FcmService : FirebaseMessagingService() {
    val CHANNEL_ID = "Channel 1"

    override fun onCreate() {
        super.onCreate()

        // Crear el canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "This is used for important notifications."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        handleNotification(message)
    }

    private fun handleNotification(message: RemoteMessage) {
        val intent = Intent(this, MainComposeActivity::class.java).apply {
            putExtra("notification", "active")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_smallicon)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

}