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
        val EXTRA_OBS3 = "obs3"
        val EXTRA_OBS4 = "obs4"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "disaster alert"
    }

    override fun doWork(): Result {
        val dataName = inputData.getString(EXTRA_NAME).toString()
        val dataObs3 = inputData.getInt(EXTRA_OBS3, 1)
        val dataObs4 = inputData.getString(EXTRA_OBS4).toString()

        val title = "Emergency Alert"
        val subTitle = "The flood warning in the $dataName area is already on $dataObs4"
        if (dataObs3 >= 3)
            showNotification(title, subTitle)
        return Result.success()
    }

    private fun showNotification(title: String, subTitle: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24).setContentTitle(title)
                .setContentText(subTitle).setPriority(NotificationCompat.PRIORITY_HIGH)
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