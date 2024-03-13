package com.neilsayok.template.ui.quantityselection.componets

import android.provider.SyncStateContract.Columns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.neilsayok.template.R

@Composable
fun  QuantityGrid(multiple : Int, onQuantitySelect : (quantity: Int)->Unit) {

    LazyVerticalGrid(columns =GridCells.Fixed(4),
        modifier = Modifier.padding(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._6sdp)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._4sdp))
        ){
        for(i in 1..25){
            item { QuantityItem(multiple*i, onQuantityClick = onQuantitySelect) }
        }

    }


}