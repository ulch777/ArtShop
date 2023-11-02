package ua.ulch.artshop.presentation.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.main.MainActivity
import ua.ulch.artshop.presentation.model.CategoryModel

private const val CHANEL_DEFAULT = "DEFAULT"
private const val NOTIFY_ID = 1
private const val CATEGORY_ID_KEY = "category_id"
private const val CATEGORY_NAME_KEY = "category_name"
private const val INTENT_TYPE = "notification"
private const val CATEGORY_TO_SHOW = "category_to_show"

class ArtShopFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var categoryId: String? = null
        var categoryName: String? = null
        val title = remoteMessage.notification!!.title
        val message = remoteMessage.notification!!.body
        if (remoteMessage.data.isNotEmpty()) {
            categoryId = remoteMessage.data[CATEGORY_ID_KEY]
            categoryName = remoteMessage.data[CATEGORY_NAME_KEY]
        }
        remoteMessage.data
        categoryId?.let {
            val category = CategoryModel(
                it.toInt(), categoryName, categoryName
            )
            notificationDefault(title, message, Gson().toJson(category))
        }
    }


    override fun onNewToken(token: String) {
    }

    private fun notificationDefault(title: String?, message: String?, categoryInfo: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = INTENT_TYPE
        intent.putExtra(CATEGORY_TO_SHOW, categoryInfo)
        val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        showNotifications1(title, message, pendingIntent)
    }

    private fun showNotifications1(
        title: String?,
        message: String?,
        pendingIntent: PendingIntent?
    ) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANEL_DEFAULT)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground))
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText(message)
            .setContentTitle(title)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANEL_DEFAULT,
                CHANEL_DEFAULT,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build())
    }
}