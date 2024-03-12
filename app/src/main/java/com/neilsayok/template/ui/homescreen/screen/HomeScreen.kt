package com.neilsayok.template.ui.homescreen.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.neilsayok.template.commonviewmodels.PreferenceViewModel
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.ui.homescreen.components.ProgressIndicatorWithSteps
import com.neilsayok.template.ui.homescreen.viewmodel.FetchPriceViewModel
import com.neilsayok.template.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavHostController,
               preferenceViewModel: PreferenceViewModel,
               fetchPriceViewModel: FetchPriceViewModel = hiltViewModel()
) {

    val getFetchPriceResponse = fetchPriceViewModel.getFetchPriceResponse
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        fetchPriceViewModel.getPriceDetails()
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            getFetchPriceResponse.collectLatest {
                with(getFetchPriceResponse.value) {
                    when (this?.status) {
                        Resource.Status.ERROR -> {
                            preferenceViewModel.hideLoadingDialog()
                            showToast(context, this.message)
                        }

                        Resource.Status.LOADING -> {
                            preferenceViewModel.showLoadingDialog()
                        }


                        else -> {
                            preferenceViewModel.hideLoadingDialog()
                        }

                    }
                }
            }
        }
    }


//    LaunchedEffect(Unit){
//        with(getFetchPriceResponse.value) {
//            when (this?.status) {
//                Resource.Status.ERROR -> {
//                    preferenceViewModel.hideLoadingDialog()
//                    showToast(context, message)
//                }
//
//                Resource.Status.LOADING -> preferenceViewModel.showLoadingDialog()
//                else ->  preferenceViewModel.hideLoadingDialog()
//            }
//        }
//    }

//    Text(text = getFetchPriceResponse.value?.data.toString())


    if (getFetchPriceResponse.value?.status == Resource.Status.SUCCESS) {
        Column {
            ProgressIndicatorWithSteps(
                min = getFetchPriceResponse.value?.data?.min,
                max = getFetchPriceResponse.value?.data?.max,
                currentValue = 5000,
                points = getFetchPriceResponse.value?.data?.points
            )
        }
    }
}