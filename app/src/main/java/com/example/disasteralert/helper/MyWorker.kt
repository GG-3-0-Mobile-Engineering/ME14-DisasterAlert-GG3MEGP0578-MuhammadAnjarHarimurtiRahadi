package com.example.disasteralert.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.disasteralert.R

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        val EXTRA_NAME = "data"
        val EXTRA_OBS = "obs"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "disaster alert"
    }

    override fun doWork(): Result {
        val dataName = inputData.getString(EXTRA_NAME)
        val dataObs = inputData.getString(EXTRA_OBS)
        showNotification(dataName.toString(), dataObs.toString())
        return Result.success()
    }

    private fun showNotification(dataName: String, dataObs: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24).setContentTitle(dataName)
                .setContentText(dataObs).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}