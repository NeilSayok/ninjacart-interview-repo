package com.neilsayok.template.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.neilsayok.template.BuildConfig
//import com.facebook.flipper.android.AndroidFlipperClient
//import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
//import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.neilsayok.template.data.datastore.IPreferenceDataStore
import com.neilsayok.template.data.datastore.PreferenceDataStoreHelper
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.data.model.common.error.ErrorEventData
import com.neilsayok.template.domain.interceptors.AuthorizationInterceptorKPN
import com.neilsayok.template.domain.services.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): IPreferenceDataStore {
        return PreferenceDataStoreHelper(context)
    }

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideAuthorizationInterceptorKPN(dataStoreHelper: PreferenceDataStoreHelper) =
        AuthorizationInterceptorKPN(dataStoreHelper)



//    @Provides
//    fun provideFlipperInterceptor(
//        @ApplicationContext context: Context
//    ): FlipperOkhttpInterceptor =
//        FlipperOkhttpInterceptor(
//            AndroidFlipperClient
//                .getInstance(context)
//                .getPluginByClass(NetworkFlipperPlugin::class.java))
//


    @SuppressLint("SuspiciousIndentation")
    @Singleton
    @Provides
    fun providesOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptorKPN,
//        flipperOkhttpInterceptor: FlipperOkhttpInterceptor,
        ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
//        builder.addNetworkInterceptor(flipperOkhttpInterceptor)

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BFF_HOST)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)


    @Singleton
    @Provides
    fun providesErrorLiveData() = MutableLiveData<Resource<ErrorEventData?>>()


}