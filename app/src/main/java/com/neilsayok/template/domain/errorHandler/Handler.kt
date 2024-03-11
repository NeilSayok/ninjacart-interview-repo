package com.neilsayok.template.domain.errorHandler

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.neilsayok.template.application.KPNStoreApplication
import com.neilsayok.template.BuildConfig
import com.neilsayok.template.data.constants.AppConstants
import com.neilsayok.template.data.datastore.PreferenceDataStoreHelper
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.data.model.common.error.ErrorEventData
import com.neilsayok.template.data.model.common.toError
import com.neilsayok.template.utils.backgroundThreadShortToast
import com.neilsayok.template.utils.getErrorEventData
import com.neilsayok.template.utils.getErrorTitleFromResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicBoolean

fun <T> coroutineExceptionHandler(
    vararg liveDatas: MutableLiveData<Resource<T>>,
    errorData: MutableLiveData<Resource<ErrorEventData?>>,
    iPreferenceHelper: PreferenceDataStoreHelper,
    isToastDisplayed: AtomicBoolean? = null,
    forceToast: Boolean = false,
    isMainApi: Boolean = false,
) = CoroutineExceptionHandler { _, exception ->

    if (BuildConfig.FLAVOR != "prod") {
        Log.e(
            "ExceptionHandler",
            "${exception.message.toString()}, ${exception.printStackTrace()} ${exception}"
        )
    }

    if (exception is UnknownHostException) {
        CoroutineScope(Dispatchers.IO).launch {
            iPreferenceHelper.setConnectedToNetwork(false)
        }
    }

    if (exception is HttpException) {
        if (exception.code() in listOf(401, 403)) {
            CoroutineScope(Dispatchers.IO).launch {
                iPreferenceHelper.setIsLoggedIn(false)
            }
        }
        val errorResponse = getErrorTitleFromResponse(
            (exception).response()?.errorBody()?.string()
        )



        errorData.apply {
            postValue(
                Resource.success(
                    getErrorEventData(
                        exception,
                        AppConstants.EMPTY_STRING,
                        errorResponse?.message.toString(),
                        errorResponse?.errorTitle.toString()
                    )
                )
            )
            if (isMainApi) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    @Suppress("Since15")
                    isToastDisplayed?.plain = true
                } else {
                    isToastDisplayed?.set(true)
                }
            }
        }

        liveDatas.forEach { liveData ->
            liveData.apply {
                postValue(
                    value.toError(
                        message = errorResponse?.message,
                        errorTitle = errorResponse?.errorTitle,
                        errorCode = errorResponse?.errorCode,
                        handled = forceToast
                    )
                )
            }
        }


        if (errorResponse != null
            && isToastDisplayed != null
            && !errorResponse.message.isNullOrBlank()
            && !isMainApi
        ) {
            if (!isToastDisplayed.get()) {
                backgroundThreadShortToast(
                    KPNStoreApplication.INSTANCE.applicationContext,
                    errorResponse.message.toString()
                )
                isToastDisplayed.set(true)
            }
        } else if (forceToast) {
            backgroundThreadShortToast(
                KPNStoreApplication.INSTANCE.applicationContext, errorResponse?.message.toString()
            )
        }


    } else {
        liveDatas.forEach { liveData ->
            liveData.apply {
                postValue(
                    value.toError(
                        message = exception.message ?: AppConstants.GLOBAL_ERROR_MESSAGE
                    )
                )
            }
        }

    }
}
