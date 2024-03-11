@file:Suppress("UNREACHABLE_CODE")

package com.neilsayok.template.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.neilsayok.template.data.constants.AppConstants
import com.neilsayok.template.data.model.common.error.ErrorData
import com.neilsayok.template.data.model.common.error.ErrorEventData
import com.neilsayok.template.data.model.common.error.ErrorTitleMsg
import retrofit2.HttpException
import java.io.IOException
import java.io.Serializable
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs


fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}



fun getReadableLocation(latitude: Double, longitude: Double, context: Context): String {
    var addressText = ""
    val geocoder = Geocoder(context, Locale.getDefault())

    try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            addressText = "${address.getAddressLine(0)}, ${address.locality}"
            Log.d("geolocation", addressText)
        }

    } catch (e: IOException) {
        Log.d("geolocation", e.message.toString())

    }
    return addressText

}

@RequiresApi(Build.VERSION_CODES.O)
fun getAMPMTiming(am_pm: String): String {
    var tempAMPM = ""
    val am = 0..12
    //val pm = 12..24
    try {
        if (!am_pm.isNullOrEmpty()) {
            val mString = am_pm.split(":").toTypedArray()
            println(mString[0])
            println(mString[1])
            if (am.contains(mString[0].toInt())) {
                tempAMPM = am_pm + "AM"
            } else {
                tempAMPM =  LocalTime.parse(am_pm).format(DateTimeFormatter.ofPattern("hh:mma"))
            }
        }
    } catch (e: IOException) {
        Log.d("ap_pm", e.message.toString())

    }
    return tempAMPM

}


fun getErrorTitleFromResponse(errorObj: String?): ErrorTitleMsg? {
    return try {
        if (!errorObj.isNullOrEmpty()) {
            val gson = Gson()
            val error: ErrorData = gson.fromJson(errorObj, ErrorData::class.java)
            val response: ErrorTitleMsg? = if (!error?.errors.isNullOrEmpty()) {
                val rawErrorMsg = if (error?.errors?.get(0)?.rawErrors != null) {
                    error?.errors?.get(0)?.rawErrors?.get(0)?.message.toString()
                } else {
                    error?.errors?.get(0)?.message.toString() ?: AppConstants.SOMETHING_WENT_WRONG
                }
                ErrorTitleMsg(
                    errorTitle = error?.errors?.get(0)?.property ?: AppConstants.EMPTY_STRING,
                    message = rawErrorMsg,
                    errorCode = error?.errors?.get(0)?.code ?: AppConstants.SOMETHING_WENT_WRONG
                )
            } else {
                ErrorTitleMsg("", "", "")
            }
            response
        } else {
            ErrorTitleMsg("", "", "")
        }
    } catch (e: JsonSyntaxException) {
        ErrorTitleMsg("", "", "")
    } catch (e: Exception) {
        ErrorTitleMsg("", "", "")
    }
}

fun getErrorEventData(
    exception: HttpException,
    pinCode: String?,
    message: String?,
    errorTitle: String?,
): ErrorEventData {
    return ErrorEventData(
        (exception).response()?.code().toString(),
        (exception).response()?.raw()?.request?.url.toString(),
        message,
        errorTitle,
        pinCode
    )
}

fun backgroundThreadShortToast(
    context: Context?,
    msg: String?,
) {
    if (context != null && msg != null) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context, msg, Toast.LENGTH_SHORT
            ).show()
        }
    }
}

fun showToast(
    context: Context?,
    msg: String?,
    duration: Int = Toast.LENGTH_SHORT
) {
    try {
        Toast.makeText(context, msg, duration).show()
    } catch (e: Exception) {
        backgroundThreadShortToast(context, msg)
    }

}

fun Fragment.setToolBar(toolBar: Toolbar?) {
    (activity as AppCompatActivity?)?.setSupportActionBar(toolBar)
}

fun Fragment.toolBar(): ActionBar? {
    return (activity as AppCompatActivity?)?.supportActionBar
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key, T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}


fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
    return emailRegex.matches(this)
}

fun String.isPhoneValid(): Boolean {
    return PhoneNumberUtils.isGlobalPhoneNumber(this)
}

fun String.toCamelCase(): String =
    this.split(" ").joinToString(separator = " ") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }


fun String.toFormattedDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val inputDate = inputFormat.parse(this)
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    outputFormat.timeZone = TimeZone.getTimeZone("UTC+10")
    return outputFormat.format(inputDate)
}


fun String.calculateTimeAgo(): String {
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(this)
        val currentTime = Date()

        val diff = currentTime.time - date?.time!!

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = days / 365

        val timeAgo = StringBuilder()

        if (years > 0) {
            timeAgo.append("$years ${if (years > 1) "years" else "year"} ")
        } else if (months > 0) {
            timeAgo.append("$months ${if (months > 1) "months" else "month"} ")
        } else if (days > 0) {
            timeAgo.append("$days ${if (days > 1) "days" else "day"} ")
        } else if (hours > 0) {
            timeAgo.append("$hours ${if (hours > 1) "hours" else "hour"} ")
        } else if (minutes > 0) {
            timeAgo.append("$minutes ${if (minutes > 1) "minutes" else "minute"} ")
        } else {
            timeAgo.append("$seconds ${if (seconds > 1) "seconds" else "second"} ")

        }

        timeAgo.append("ago")
        return timeAgo.toString().trim()
    } catch (e: Exception) {
        e.printStackTrace()
        return AppConstants.EMPTY_STRING
    }
}

fun Double.toInrString(): String {

    val indiaLocale = Locale("en", "IN")
    val indianRupee = Currency.getInstance("INR")
    val currencyFormat = NumberFormat.getCurrencyInstance(indiaLocale)
    currencyFormat.currency = indianRupee
    return currencyFormat.format(this)
}

fun Long.toFormattedTimeStamp(): String {
    val date = Date(this)
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.time = date

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)
    val milliseconds = this % 1000

    val timeZoneOffset = String.format(
        "%s%02d:%02d",
        if (calendar.get(Calendar.ZONE_OFFSET) < 0) "-" else "+",
        abs(calendar.get(Calendar.ZONE_OFFSET)) / (60 * 60 * 1000),
        abs(calendar.get(Calendar.ZONE_OFFSET)) / (60 * 1000) % 60
    )

    return String.format(
        "%04d-%02d-%02dT%02d:%02d:%02d.%03d%s",
        year, month, day, hours, minutes, seconds, milliseconds, timeZoneOffset
    )
}
fun callPageEvent(pageName:String){
    AppsEventsForAnalytics.trackEvent(pageName)
    Log.d("firebase-page-trace", pageName)
}



