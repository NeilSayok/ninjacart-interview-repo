package com.neilsayok.template.ui.commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.neilsayok.template.theme.CardGradientBgTrans


@Composable
fun StoreLoader(showLoader : Boolean?) {
    if (showLoader == true) {
        val interactionSource = remember { MutableInteractionSource() }

        Box(
            modifier = Modifier
                .background(CardGradientBgTrans)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { }
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}