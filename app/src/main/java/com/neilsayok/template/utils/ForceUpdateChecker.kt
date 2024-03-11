package com.neilsayok.template.utils

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class ForceUpdateChecker(
    context: Context,
    onUpdateNeededListener: OnUpdateNeededListener?
) {
    private val onUpdateNeededListener: OnUpdateNeededListener?
    private val context: Context

    interface OnUpdateNeededListener {
        fun onUpdateNeeded(
            updateUrl: String?,
            forceUpdate: Boolean?,
            currentVersion: String?,
            appVersion: String?
        )
    }

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            val forceUpdate = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)
            val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION)
            val appVersion = getAppVersion(context)
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
            if (!TextUtils.equals(currentVersion, appVersion)
                && onUpdateNeededListener != null
            ) {
                onUpdateNeededListener.onUpdateNeeded(
                    updateUrl,
                    forceUpdate,
                    currentVersion,
                    appVersion
                )
            }
        }
    }

    private fun getAppVersion(context: Context): String {
        var result = ""
        try {
            result = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            e.message?.let { Log.e(TAG, it) }
        }
        Log.i("app_version:", result)
        return result
    }

    class Builder(context: Context) {
        private val context: Context
        private var onUpdateNeededListener: OnUpdateNeededListener? = null
        fun onUpdateNeeded(onUpdateNeededListener: OnUpdateNeededListener?): Builder {
            this.onUpdateNeededListener = onUpdateNeededListener
            return this
        }

        private fun build(): ForceUpdateChecker {
            return ForceUpdateChecker(context, onUpdateNeededListener)
        }

        fun check(): ForceUpdateChecker {
            val forceUpdateChecker = build()
            forceUpdateChecker.check()
            return forceUpdateChecker
        }

        init {
            this.context = context
        }
    }

    companion object {
        private val TAG = ForceUpdateChecker::class.java.simpleName
        const val KEY_UPDATE_REQUIRED = "store_force_update_app"
        const val KEY_CURRENT_VERSION = "store_app_minimum_version"
        const val KEY_UPDATE_URL = "store_app_force_update_store_url"
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    init {
        this.context = context
        this.onUpdateNeededListener = onUpdateNeededListener
    }
}