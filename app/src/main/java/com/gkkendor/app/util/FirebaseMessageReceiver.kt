package com.gkkendor.app.util

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gkkendor.app.R
import com.gkkendor.app.ui.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.random.Random

class FirebaseMessageReceiver : FirebaseMessagingService() {
    private val executor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private lateinit var broadcaster: LocalBroadcastManager

    companion object {
        private const val TAG = "FirebaseMessageReceiver"
    }

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Constants.setTokenFCM(token) {
            // Log Token
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
        }
    }

    private var currentEmailSender: String? = null

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        CoroutineScope(executor).launch {
            val isPanicActive = message.data["isPanicActive"] ?: "false"
            if (isPanicActive == "true") {

                val currentLocation = message.data["currentLocation"]
                val emailSender = message.data["emailSender"]

                if (currentEmailSender == null) {
                    currentEmailSender = emailSender
                }

                if (message.data.containsKey("title") && message.data.containsKey("body")) {
                    sendNotification(
                        message.messageId,
                        "${message.data["title"]}",
                        """
                            ${message.data["body"]}
                            $currentLocation
                        """.trimIndent()
                    )
                } else {
                    /*
                    val intent = Intent("MyData")
                    intent.putExtra("phone", message.getData().get("DriverPhone"))
                    intent.putExtra("lat", message.getData().get("DriverLatitude"))
                    intent.putExtra("lng", message.getData().get("DriverLongitude"))
                    broadcaster.sendBroadcast(intent)
                    */
                    if (currentEmailSender == emailSender) {
                        val intent = Intent("MyData")
                            .putExtra("currentLocation", currentLocation)
                            .putExtra("emailSender", emailSender)
                        broadcaster.sendBroadcast(intent)
                    }
                }
            } else {
                currentEmailSender = null
            }

            Log.e(TAG, "sender ${message.senderId}")
            Log.e(TAG, "from = ${message.from}")
            Log.e(TAG, "Notification = ${convertString(message.notification)}")
            Log.e(TAG, "currentLocation = ${message.data.get("currentLocation")}")
            Log.e(TAG, "emailSender = ${message.data.get("emailSender")}")
        }
    }

    private fun sendNotification(messageId: String?, title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = createNotificationChannel()
        val id = if (messageId != null) {
            messageId.toIntOrNull() ?: Random.nextInt()
        } else {
            Random.nextInt()
        }
        notificationManager.notify(id, notificationBuilder.build())
    }

    private fun convertString(notification: RemoteMessage.Notification?): String {
        return "tittle = ${notification?.title}, body = ${notification?.body}"
    }

    override fun onDestroy() {
        super.onDestroy()
        // same like executor.shutdown()
        this.executor.close()
    }
}