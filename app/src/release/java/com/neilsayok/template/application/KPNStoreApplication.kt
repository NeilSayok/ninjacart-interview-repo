package com.neilsayok.template.application

//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.plugins.inspector.DescriptorMapping
//import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
//import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
//import com.facebook.soloader.SoLoader
import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.neilsayok.template.utils.AppsEventsForAnalytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KPNStoreApplication : Application() {

    companion object {
        lateinit var INSTANCE: KPNStoreApplication
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initFirebaseRemoteConfig()
        AppsEventsForAnalytics.init()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initFlipper()
    }

    private fun initFirebaseRemoteConfig() {
        initRemoteConfig()
    }

    private fun initFlipper() {

//        SoLoader.init(this, false)
//        val client = AndroidFlipperClient.getInstance(this)
//        client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//        client.addPlugin(NetworkFlipperPlugin())
//        client.start()

    }


    private fun initRemoteConfig() {
        try {
            val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "remote config is fetched.")
                        firebaseRemoteConfig.fetchAndActivate()
                    }
                }
        } catch (e: Exception) {
            Log.e("initRemoteConfig", e.toString())
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i("Application", "onLowMemory")
    }


}