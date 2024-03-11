package com.neilsayok.template.data.model.common.error

import android.os.Parcelable

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class ErrorEventData(
    var code: String?,
    var url: String?,
    var errorMsg: String?,
    var errorTitle: String?,
    var pincode: String?
) : Parcelable

@Keep
@Parcelize
data class ErrorsList(
    @SerializedName("message") val message: String?,
    @SerializedName("property") val property: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("rawErrors") val rawErrors: List<RawError?>?,
    @SerializedName("title") val title: String?
) : Parcelable

@Keep
@Parcelize
data class ErrorTitleMsg(
    @SerializedName("title") val errorTitle: String?,
    @SerializedName("code") val errorCode: String?,
    @SerializedName("message") val message: String?
) : Parcelable

@Keep
@Parcelize
data class ErrorData(
    @SerializedName("errors")
    val errors: List<Error?>?
) : Parcelable

@Keep
@Parcelize
data class Error(
    @SerializedName("code")
    val code: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("rawErrors")
    val rawErrors: List<RawError?>?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("property")
    val property: String?
) : Parcelable

@Keep
@Parcelize
data class RawError(
    @SerializedName("code")
    val code: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("title")
    val title: String?
) : Parcelable

