package com.neilsayok.template.commonviewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neilsayok.template.data.datastore.PreferenceDataStoreHelper
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.data.model.common.error.ErrorEventData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(
    val preferenceDataStoreHelper: PreferenceDataStoreHelper,
    val errorData: MutableStateFlow<Resource<ErrorEventData?>?>,
    ) : ViewModel() {

    val showLoader = MutableLiveData(false)



    fun showLoadingDialog(){
        showLoader.postValue(true)
    }
    fun hideLoadingDialog(){
        showLoader.postValue(false)
    }




}