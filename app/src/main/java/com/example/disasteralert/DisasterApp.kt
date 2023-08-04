package com.example.disasteralert

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DisasterApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var networkFlipperPlugin: NetworkFlipperPlugin

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) initFlipper()
    }

    private fun initFlipper() {
        SoLoader.init(this, false)

        val client = AndroidFlipperClient.getInstance(this.applicationContext).apply {
            addPlugin(
                InspectorFlipperPlugin(
                    applicationContext, DescriptorMapping.withDefaults()
                )
            )
            addPlugin(CrashReporterPlugin.getInstance())
            addPlugin(networkFlipperPlugin)
        }
        client.start()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

}