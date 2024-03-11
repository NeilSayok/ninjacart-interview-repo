package com.neilsayok.template.utils

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object AppsEventsForAnalytics {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init() {
        firebaseAnalytics = Firebase.analytics
    }

    fun trackEvent(eventName: String) {
        val job = Job()
        val coroutineScope = CoroutineScope(job)
        coroutineScope.launch {
            firebaseAnalyticsEvent(eventName)
            Log.d("event", eventName)
        }

        firebaseAnalyticsEvent(eventName)
        Log.d("event", eventName)
    }

    fun trackEvent(eventName: String, bundle: Bundle) {
        val job = Job()
        val coroutineScope = CoroutineScope(job)
        coroutineScope.launch {
            firebaseAnalyticsEvent(eventName, bundle)
            Log.d("event", eventName + "\n" + bundle.toString())
        }

    }

    private fun firebaseAnalyticsEvent(eventName: String) {
        val bundle = Bundle()
        if (this::firebaseAnalytics.isInitialized) {
            firebaseAnalytics.logEvent(eventName, bundle)
        }
    }

    private fun firebaseAnalyticsEvent(eventName: String, params: Bundle) {
        if (this::firebaseAnalytics.isInitialized) {
            firebaseAnalytics.logEvent(eventName, params)
        }
    }
}