package com.neilsayok.template.data.model.common

import androidx.lifecycle.MutableLiveData


data class Resource<out T>(
    var status: Status,
    val data: T? = null,
    val message: String? = "",
    val errorTitle: String? = "",
    val errorCode: String? = "",
    var handled: Boolean? = null,
    var pos: Int? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        NETWORK_DISCONNECTED,
        LOADING,
        NULL
    }


    companion object {
        fun <T> success(
            data: T? = null,
            message: String? = "",
            errorTitle: String? = "",
            errorCode: String? = "",
            pos: Int? = null
        ): Resource<T> {
            return Resource(Status.SUCCESS, data, message, errorTitle, errorCode, pos = pos)
        }

        @JvmOverloads
        fun <T> error(
            message: String? = "",
            errorTitle: String? = "",
            errorCode: String? = "",
            data: T? = null
        ): Resource<T> {
            return Resource(
                Status.ERROR,
                data = data,
                message = message,
                errorTitle = errorTitle,
                errorCode = errorCode
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data)
        }
    }


}

fun <T> Resource<T>?.append(oldResource: Resource<T>): Resource<T> {

    return Resource.loading()
}

fun <T> Resource<T>?.toLoading(handled: Boolean? = null, pos: Int? = null): Resource<T> {
    return this?.copy(status = Resource.Status.LOADING, handled = handled, pos = pos, data = data)
        ?: Resource.loading()
}


fun <T> Resource<T>?.toError(
    data: T? = null,
    message: String? = "",
    errorTitle: String? = "",
    errorCode: String? = "",
    handled: Boolean? = null,
    pos: Int? = null
): Resource<T> {
    return this?.copy(
        status = Resource.Status.ERROR,
        message = message,
        errorTitle = errorTitle,
        errorCode = errorCode,
        handled = handled,
        pos = pos
    )
        ?: Resource.error(message, errorTitle, errorCode, data)
}


fun <T> MutableLiveData<Resource<T>>.toLoading(handled: Boolean? = null, pos: Int? = null) {
    postValue(value.toLoading(handled, pos))
}

@JvmName("toLoadingNullable")
fun <T> MutableLiveData<Resource<T>?>.toLoading(handled: Boolean? = null, pos: Int? = null) {
    postValue(value.toLoading(handled, pos))
}

fun <T> MutableLiveData<Resource<T>>.toError(
    data: T? = null,
    message: String? = "",
    errorTitle: String? = "",
    errorCode: String? = "",
    handled: Boolean? = null,
    pos: Int? = null
) {
    postValue(value.toError(data, message, errorTitle, errorCode, handled, pos))
}

@JvmName("toErrorNullable")
fun <T> MutableLiveData<Resource<T>?>.toError(
    data: T? = null,
    message: String? = "",
    errorTitle: String? = "",
    handled: Boolean? = null,
    pos: Int? = null
) {
    postValue(
        value.toError(
            data = data,
            message = message,
            errorTitle = errorTitle,
            handled = handled,
            pos = pos
        )
    )
}
