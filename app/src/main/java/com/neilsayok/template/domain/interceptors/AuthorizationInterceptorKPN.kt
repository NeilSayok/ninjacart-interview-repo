package com.neilsayok.template.domain.interceptors


import android.util.Log
import com.neilsayok.template.data.constants.AppConstants
import com.neilsayok.template.data.datastore.PreferenceDataStoreHelper
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptorKPN @Inject constructor(
    private val dataStoreHelper: PreferenceDataStoreHelper
) : Interceptor {
    private var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            token = dataStoreHelper.getSessionToken()
        }

        Log.d("Token", token?: AppConstants.EMPTY_STRING)

        val request = chain.request()
        val originalHttpUrl: HttpUrl = request.url
        val url = originalHttpUrl.newBuilder()
//            .addQueryParameter(API_HEADER_CHANNEL, API_HEADER_CHANNEL_VALUE)
//            .addQueryParameter(APP_VERSION_NAME, BuildConfig.VERSION_NAME)


        val requestBuilder = request.newBuilder()
            .url(url.build())

////        if (!token.isNullOrBlank()) {
////            requestBuilder.addHeader(
////                ApiConstants.API_HEADER_AUTHORIZATION,
////                "${ApiConstants.API_HEADER_AUTHORIZATION_VALUE} $token"
////            )
////        }
//        // TODO Remove else block
//        else {
//            requestBuilder.addHeader(
//                ApiConstants.API_HEADER_AUTHORIZATION,
//                "Something"
//            )
//        }
//        if (BuildConfig.VERSION_NAME.isNotBlank()
//            // TODO REMOVE the and statement
//            && !url.toString().contains("outlets")
//            ) {
//            requestBuilder.addHeader(
//                ApiConstants.API_APP_VERSION,
//                BuildConfig.VERSION_NAME
//            )
//        }
//
//
//        runBlocking {
//            if (dataStoreHelper.getUUID().isNotEmpty()) {
//                requestBuilder.addHeader(
//                    ApiConstants.API_USER_JOURNEY_ID,
//                    dataStoreHelper.getUUID()
//                )
//            }
//        }


        val response = chain.proceed(requestBuilder.build())
        if (response.isSuccessful) {
            return response
        }
        return response

    }

}
