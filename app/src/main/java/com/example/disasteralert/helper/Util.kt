package com.example.disasteralert.helper

import android.content.Context
import androidx.work.*
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesGeometriesItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object Util {

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    const val WORKER_TAG = "my_notif_worker"

    fun getDateApiFormat(date: String): String {
        return date + "T00:00:00+0700"
    }

    fun getDatePresentationFormat(date: String): String {
        val dateTransform = LocalDateTime.parse(date.substring(0, 19))
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
        val formatted = dateTransform.format(formatter)
        return formatted.toString()
    }

    fun getProvinceName(placeFormat: String): String = Constant.AREA[placeFormat].toString()

    fun getLatLngFormat(lat: Double, lon: Double) = LatLng(lat, lon)

    fun moveCameraAction(
        mMap: GoogleMap, builder: LatLngBounds.Builder?, location: LatLng?, zoom: Float = 14f
    ) {
        if (location != null) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        else if (builder != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))
        }
    }

    fun placeMarkerOnMap(mMap: GoogleMap, disasterData: List<DisasterEntity>) {
        val builder = LatLngBounds.Builder()
        disasterData.forEach { disasterItem ->
            val position = getLatLngFormat(
                disasterItem.latitude, disasterItem.longitude
            )
            builder.include(position)
            val markerOptions = MarkerOptions().position(position)
            val areaCode = disasterItem.disasterLoc
            val provinceName = Constant.AREA[areaCode]
            markerOptions.title(disasterItem.disasterType + " (" + provinceName + ")")
            mMap.addMarker(markerOptions)
        }

        moveCameraAction(mMap = mMap, builder = builder, location = null)
    }

    fun setWorkManager(context: Context) {
        workManager = WorkManager.getInstance(context)
    }

    fun setPeriodicWorkManager() {
//        val gaugeName = floodGaugesItem.floodGaugesProperties.gaugenameid
//        val observation = floodGaugesItem.floodGaugesProperties.observations.last()
//        val data = Data.Builder()
//            .putString(MyWorker.EXTRA_NAME, gaugeName)
//            .putInt(MyWorker.EXTRA_OBS3, observation.f3)
//            .putString(MyWorker.EXTRA_OBS4, observation.f4)
//            .build()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 60, TimeUnit.MINUTES)
                .setConstraints(constraints).addTag(WORKER_TAG).build()
        workManager.enqueueUniquePeriodicWork(
            WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest
        )
    }

    fun cancelWorkManager() {
        workManager.cancelUniqueWork(WORKER_TAG)
    }

    fun getAreaCode(selectedItem: String): String {
        return if (selectedItem.isNotBlank()) Constant.AREA.entries.find { it.value == selectedItem }?.key.toString()
        else ""
    }
}