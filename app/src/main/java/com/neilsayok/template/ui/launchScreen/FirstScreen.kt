package com.neilsayok.template.ui.launchScreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.neilsayok.template.commonviewmodels.PreferenceViewModel
import kotlinx.coroutines.delay


@Composable
fun FirstScreen(navController: NavHostController,
                preferenceViewModel: PreferenceViewModel
) {

    LaunchedEffect(Unit){
        delay(5000)
        preferenceViewModel.hideLoadingDialog()
    }
    Text(
        text = "Hello Sayok!",
    )
}