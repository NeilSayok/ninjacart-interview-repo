package com.neilsayok.template.ui.homescreen.viewmodel

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neilsayok.template.data.datastore.PreferenceDataStoreHelper
import com.neilsayok.template.data.model.FetchPriceResponse
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.data.model.common.error.ErrorEventData
import com.neilsayok.template.data.model.common.toLoading
import com.neilsayok.template.domain.errorHandler.coroutineExceptionHandlerWithFlow
import com.neilsayok.template.domain.repositories.FetchPriceRepository
import com.neilsayok.template.domain.services.ApiInterface
import com.neilsayok.template.utils.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FetchPriceViewModel @Inject constructor(
    val fetchPriceRepository: FetchPriceRepository,
    val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    val errorData: MutableStateFlow<Resource<ErrorEventData?>?>,
    ) : ViewModel() {


    private val _getFetchPriceResponse =
        MutableStateFlow<Resource<FetchPriceResponse>?>(Resource(Resource.Status.NULL, null))
    val getFetchPriceResponse = _getFetchPriceResponse.asStateFlow()

    val priceMap = SnapshotStateMap<Int,Int>()

    fun getPriceDetails() {
        val exceptionHandler = coroutineExceptionHandlerWithFlow(
            viewModelScope,
            _getFetchPriceResponse,
            errorData = errorData,
            iPreferenceHelper = preferenceDataStoreHelper,
            forceToast = true
        )

        viewModelScope.launch {
            _getFetchPriceResponse.apply {
                emit(value.toLoading())
            }
        }
        viewModelScope.launch(DispatchersProvider.IO + exceptionHandler) {
            val apiResponseData = fetchPriceRepository.fetchPriceResponse()
            _getFetchPriceResponse.emit(Resource.success(apiResponseData) as Resource<FetchPriceResponse>?)
        }
    }






}