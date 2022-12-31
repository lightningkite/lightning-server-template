package com.lightningkite.template.notifications


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.AnyRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.lightningkite.rx.optional
import com.lightningkite.rx.viewgenerators.ViewGenerator
import com.lightningkite.rx.viewgenerators.fcm.VGFCMService
import com.lightningkite.template.R
import java.util.*

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : VGFCMService() {
    override val icon: Int
        get() = R.drawable.ic_notification

    companion object {
        fun makeChannels(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(NotificationChannel(
                    "default",
                    "Default",
                    NotificationManager.IMPORTANCE_HIGH
                ))
            }
        }
    }
}
