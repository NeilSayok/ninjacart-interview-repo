package com.neilsayok.template.domain.errorHandler

import java.io.Serializable

sealed class ErrorStatus : Serializable {
    object NETWORK_ERROR : ErrorStatus() {
        override fun toString(): String = "NETWORK_ERROR"
    }

    object APP_UNDER_MAINTAINENCE : ErrorStatus() {
        override fun toString(): String = "APP_UNDER_MAINTAINENCE"
    }

    object APP_FORCE_UPDATE : ErrorStatus() {
        override fun toString(): String = "APP_FORCE_UPDATE"
    }
    object NONE : ErrorStatus() {
        override fun toString(): String = "NONE"
    }


}


