package com.neilsayok.template.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object DispatchersProvider {

    var unitTestCoroutineScope: CoroutineScope? = null

    private var unitTestDispatcher: CoroutineDispatcher? = null

    val IO
        get() = unitTestDispatcher ?: Dispatchers.IO
}