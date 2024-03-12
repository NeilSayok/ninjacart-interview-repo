package com.neilsayok.template.data.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class FetchPriceResponse(
    @SerializedName("items") val items: List<Item?>?,
    @SerializedName("max") val max: Int?,
    @SerializedName("min") val min: Int?,
    @SerializedName("points") val points: List<Point?>?
) : Parcelable

@Keep
@Parcelize
data class Item(
    @SerializedName("eachQtyValue") val eachQtyValue: Int?,
    @SerializedName("multiple") val multiple: Int?,
    @SerializedName("name") val name: String?
) : Parcelable

@Keep
@Parcelize
data class Point(
    @SerializedName("value") val value: Int?
) : Parcelable
