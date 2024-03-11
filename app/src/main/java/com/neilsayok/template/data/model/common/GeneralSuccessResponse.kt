package com.neilsayok.template.data.model.common


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GeneralSuccessResponse(
    @SerializedName("success")
    val success: Boolean?
)