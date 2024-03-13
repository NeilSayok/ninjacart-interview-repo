package com.neilsayok.template.ui.quantityselection.componets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neilsayok.template.R
import com.neilsayok.template.theme.AppFont
import com.neilsayok.template.theme.Primary
import com.neilsayok.template.theme.PrimaryTransparent
import com.neilsayok.template.utils.fontDimensionResource

@Composable
fun QuantityItem(quantity : Int,
                 onQuantityClick : (Int)-> Unit) {
    Card(border = BorderStroke(1.dp, Primary),
        backgroundColor = PrimaryTransparent,
        modifier = Modifier.clickable {
            onQuantityClick(quantity)
        }
        )
    {

        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = com.intuit.sdp.R.dimen._6sdp),
                vertical = dimensionResource(id = com.intuit.sdp.R.dimen._4sdp)
            )
        ){
            Text(
                text = quantity.toString(),
                fontFamily = AppFont,
                fontWeight = FontWeight.Medium,
                color = Primary,
                fontSize = fontDimensionResource(id = com.intuit.ssp.R.dimen._14ssp)
            )
        }

    }
}