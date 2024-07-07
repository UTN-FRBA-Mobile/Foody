package ar.edu.utn.frba.foody.ui.dataBase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ar.edu.utn.frba.foody.MainComposeActivity
import ar.edu.utn.frba.foody.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showNotification(message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showNotification(message: RemoteMessage) {
        val clickIntent = Intent(this, MainComposeActivity::class.java)
        val clickPendingIntent = PendingIntent.getActivity(this,1,clickIntent,PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, MainComposeActivity.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.notification_smallicon)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }
}