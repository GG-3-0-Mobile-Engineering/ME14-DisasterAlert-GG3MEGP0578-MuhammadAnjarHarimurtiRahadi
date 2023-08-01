package com.example.disasteralert.helper

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.work.*
import com.example.disasteralert.data.remote.response.disasterresponse.GeometriesItem
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesGeometriesItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object Util {

    fun getDateApiFormat(date: String): String {
        return date + "T00:00:00+0700"
    }

    fun getDatePresentationFormat(date: String): String {
        val dateTransform = LocalDateTime.parse(date.substring(0,19))
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")
        val formatted = dateTransform.format(formatter)
        return formatted.toString()
    }

    fun getProvinceName(placeFormat: String): String = Constant.AREA[placeFormat].toString()

    fun getLatLngFormat(lat: Double, lon: Double) = LatLng(lat,lon)

    fun moveLocationButton(mapFragment: SupportMapFragment, mMap: GoogleMap) {
        val locationButton =
            (mapFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams

        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)

        rlp.addRule(RelativeLayout.ALIGN_PARENT_END, 0)
        rlp.addRule(RelativeLayout.ALIGN_END, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        rlp.setMargins(30, 0, 0, 75)


    }

    fun moveCameraAction(mMap: GoogleMap, builder: LatLngBounds.Builder?, location: LatLng?, zoom: Float = 14f) {
        if (location != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        else if (builder != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))
        }
    }

    fun placeMarkerOnMap(mMap: GoogleMap, disasterData: List<GeometriesItem>) {
        val builder = LatLngBounds.Builder()
        disasterData.forEach { disasterItem ->
            val position = Util.getLatLngFormat(
                disasterItem.coordinates[1] as Double, disasterItem.coordinates[0] as Double
            )
            builder.include(position)
            val markerOptions = MarkerOptions().position(position)
            val areaCode = disasterItem.properties.tags.instanceRegionCode
            val provinceName = Constant.AREA[areaCode]
            markerOptions.title(provinceName)
            mMap.addMarker(markerOptions)
        }

        moveCameraAction(mMap = mMap, builder = builder, location = null)
    }

    fun setPeriodicWorkManager(context: Context ,floodGaugesItem: FloodGaugesGeometriesItem) {
        val gaugeName = floodGaugesItem.floodGaugesProperties.gaugenameid
        val observation = floodGaugesItem.floodGaugesProperties.observations.last()
        var workManager: WorkManager = WorkManager.getInstance(context)
        val data = Data.Builder().putString(
            MyWorker.EXTRA_NAME,
            gaugeName
        ).putString(MyWorker.EXTRA_OBS, observation.f4).build()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        if (observation.f3 >= 3) {
            val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
                MyWorker::class.java, 60, TimeUnit.MINUTES
            ).setInputData(data).setConstraints(constraints).build()
            workManager.enqueue(periodicWorkRequest)
        }
    }
}