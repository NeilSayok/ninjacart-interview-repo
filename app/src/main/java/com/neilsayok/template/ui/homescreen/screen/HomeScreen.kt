package com.neilsayok.template.ui.homescreen.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.neilsayok.template.commonviewmodels.PreferenceViewModel
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.ui.homescreen.viewmodel.FetchPriceViewModel
import com.neilsayok.template.utils.showToast


@Composable
fun HomeScreen(navController: NavHostController,
               preferenceViewModel: PreferenceViewModel,
               fetchPriceViewModel: FetchPriceViewModel = hiltViewModel()
) {

    val getFetchPriceResponse = fetchPriceViewModel.getFetchPriceResponse.value
    val context = LocalContext.current

    LaunchedEffect(Unit){
        fetchPriceViewModel.getPriceDetails()
    }

    LaunchedEffect(getFetchPriceResponse){
        with(getFetchPriceResponse) {
            when (this?.status) {
                Resource.Status.ERROR -> {
                    preferenceViewModel.hideLoadingDialog()
                    showToast(context, message)
                }

                Resource.Status.LOADING -> preferenceViewModel.showLoadingDialog()
                else ->  preferenceViewModel.hideLoadingDialog()
            }
        }
    }


    Text(
        text = getFetchPriceResponse?.data.toString(),
    )

}