package com.example.projectinterface

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.projectinterface.CovidUpdate.getTimeAgo
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class NotificationWorker(
    private val context: Context,
    p: WorkerParameters
) : CoroutineWorker(context, p) {

    @SuppressLint("StringFormatInvalid")
    private fun showNotification(tc: String, t: String) {
        val intent = Intent(context, CovidUpdate::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setColor(ContextCompat.getColor(context, R.color.dark_red))
            .setSmallIcon(R.drawable.ic_stat_notification_icon)
            .setContentTitle(context.getString(R.string.text_confirmed_cases, tc))
            .setContentText(context.getString(R.string.text_last_updated, t))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val nm =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            nm.createNotificationChannel(channel)
        }

        nm.notify(0, notificationBuilder.build())
    }

    override suspend fun doWork(): Result = coroutineScope {
        val r = withContext(Dispatchers.IO) { Client.api.clone().execute() }
        if (r.isSuccessful) {
            val res = Gson().fromJson(r.body?.string(), Response::class.java)
            val td = res.statewise[0]

            showNotification(
                td.confirmed ?: "",
                getTimeAgo(
                    SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        .parse(td.lastupdatedtime)
                )
            )

            Result.success()
        } else {
            Result.retry()
        }


    }
}