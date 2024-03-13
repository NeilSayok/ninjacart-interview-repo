package com.neilsayok.template.ui.homescreen.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.intuit.sdp.R
import com.neilsayok.template.commonviewmodels.PreferenceViewModel
import com.neilsayok.template.data.model.Item
import com.neilsayok.template.data.model.common.Resource
import com.neilsayok.template.ui.homescreen.components.ProgressIndicatorWithSteps
import com.neilsayok.template.ui.homescreen.components.StepperView
import com.neilsayok.template.ui.homescreen.viewmodel.FetchPriceViewModel
import com.neilsayok.template.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


fun addItemToList(list: MutableList<Item?>, newItem: Item): List<Item?> {
    Log.d("addItemToList", list.toString())
    val existingItem = list.find {
        it?.name == newItem.name && it?.multiple == newItem.multiple && it?.eachQtyValue == newItem.eachQtyValue
    }
    if (existingItem != null) {
        // Update the existing item's selectedQuantity
        list.remove(existingItem)

    }
    list.add(newItem)
    Log.d("addItemToList", list.toString())
    return list
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    preferenceViewModel: PreferenceViewModel,
    fetchPriceViewModel: FetchPriceViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        fetchPriceViewModel.getPriceDetails()
    }


    val getFetchPriceResponse = fetchPriceViewModel.getFetchPriceResponse
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    Log.d("item", fetchPriceViewModel.itemSet.toList().toString())
    var totalPrice by remember {
        mutableStateOf(
            getFetchPriceResponse.value?.data?.min?:0
        )
    }

    Log.d("totalPrice", totalPrice.toString())


    LaunchedEffect(totalPrice) {
        Log.d("totalprice", totalPrice.toString())
        fetchPriceViewModel.showToastForPoint.forEach {
            if (totalPrice >= it.key && !it.value) {
                showToast(context, "Yay ${it.key}")
                fetchPriceViewModel.showToastForPoint[it.key] = true

            }
            if (totalPrice < it.key && it.value) {
                fetchPriceViewModel.showToastForPoint[it.key] = false
            }
        }

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

                        Resource.Status.SUCCESS -> {
                            preferenceViewModel.hideLoadingDialog()
                            this?.data?.points?.forEach {
                                if (it?.value != null) {
                                    fetchPriceViewModel.showToastForPoint.put(it.value, false)
                                }
                            }
                            this.data?.max?.let { max ->
                                fetchPriceViewModel.showToastForPoint.put(
                                    max, false)
                            }
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
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen._16sdp))) {
            ProgressIndicatorWithSteps(
                min = getFetchPriceResponse.value?.data?.min,
                max = getFetchPriceResponse.value?.data?.max,
                currentValue = totalPrice,
                points = getFetchPriceResponse.value?.data?.points
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)))

            getFetchPriceResponse.value?.data?.items?.forEach {
                Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))
                StepperView(item = it, max = getFetchPriceResponse.value?.data?.max, totalPrice = totalPrice, onValueChange = { item ->
                    Log.d("onValueChange", "$item")
                    if (item != null) {
                        val newList =
                            addItemToList(fetchPriceViewModel.itemSet.toMutableList(), item)
                        fetchPriceViewModel.itemSet.clear()
                        fetchPriceViewModel.itemSet.addAll(newList)
                        totalPrice = 0
                        totalPrice = fetchPriceViewModel.itemSet.sumOf {
                            (it?.selectedQuantity ?: 0) * (it?.eachQtyValue ?: 0)
                        }
                    }
                })
            }


        }
    }
}