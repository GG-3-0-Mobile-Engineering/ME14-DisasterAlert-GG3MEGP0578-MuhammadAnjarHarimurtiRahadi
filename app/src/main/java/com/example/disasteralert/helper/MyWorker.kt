package com.example.disasteralert.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.disasteralert.R
import com.example.disasteralert.data.Results
import com.example.disasteralert.domain.repository.DisasterRepository
import com.example.disasteralert.helper.Constant.CHANNEL_ID
import com.example.disasteralert.helper.Constant.CHANNEL_NAME
import com.example.disasteralert.helper.Constant.NOTIFICATION_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MyWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val repository: DisasterRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        getFloodGaugesData()
        return Result.success()
    }

    private suspend fun getFloodGaugesData() {
        when (val floodGauges = repository.getFloodGaugesData()) {
            is Results.Success -> {
                val floodGaugesData = floodGauges.data
                var gaugesName = ""
                var observation = ""

                for (floodGaugesItem in floodGaugesData) {
                    val gaugesProperties = floodGaugesItem.floodGaugesProperties
                    if (gaugesProperties.observations.last().f3 >= 3) {
                        gaugesName = gaugesProperties.gaugenameid
                        observation = gaugesProperties.observations.last().f4
                    }
                }

                if (gaugesName.isNotEmpty() && observation.isNotEmpty()) {
                    val title = "Emergency Alert"
                    val subTitle = "The flood warning in the $gaugesName area is already on $observation"
                    showNotification(title, subTitle)
                }
            }
            else -> {}
        }
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