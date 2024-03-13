package com.neilsayok.template.ui.homescreen.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.intuit.sdp.R
import com.neilsayok.template.commonviewmodels.PreferenceViewModel
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.ui.homescreen.components.ProgressIndicatorWithSteps
import com.neilsayok.template.ui.homescreen.components.StepperView
import com.neilsayok.template.ui.homescreen.viewmodel.FetchPriceViewModel
import com.neilsayok.template.ui.quantityselection.componets.QuantityGrid
import com.neilsayok.template.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavHostController,
               preferenceViewModel: PreferenceViewModel,
               fetchPriceViewModel: FetchPriceViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        fetchPriceViewModel.getPriceDetails()
    }


    val getFetchPriceResponse = fetchPriceViewModel.getFetchPriceResponse
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


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









    if (getFetchPriceResponse.value?.status == Resource.Status.SUCCESS) {
        Column {
            ProgressIndicatorWithSteps(
                min = getFetchPriceResponse.value?.data?.min,
                max = getFetchPriceResponse.value?.data?.max,
                currentValue = 0,
                points = getFetchPriceResponse.value?.data?.points
            )

            getFetchPriceResponse.value?.data?.items?.forEach {
                Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))
                StepperView(item = it,
                    onValueChange = { quantity, multiplier ->
                        Log.d("onValueChange", "$quantity, $multiplier")
                    })
            }



        }
    }
}