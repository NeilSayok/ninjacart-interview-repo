package com.neilsayok.template.ui.homescreen.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.intuit.sdp.R
import com.neilsayok.template.data.model.Item
import com.neilsayok.template.theme.AppFont
import com.neilsayok.template.theme.FontPrimaryDark
import com.neilsayok.template.theme.Primary
import com.neilsayok.template.ui.quantityselection.componets.QuantityGrid
import com.neilsayok.template.utils.fontDimensionResource
import com.neilsayok.template.utils.showToast
import com.neilsayok.template.utils.toCamelCase


@Composable
fun StepperView(
    item: Item?,
    onValueChange: (item: Item?) -> Unit,
    max: Int?,
    totalPrice : Int?
) {
    val context = LocalContext.current
    var currentValue by remember {
        mutableStateOf(0)
    }
    var openAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentValue) {
        item?.selectedQuantity = currentValue
        onValueChange(item)
    }

    if (openAlertDialog)
        Dialog(onDismissRequest = { openAlertDialog = false }) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen._20sdp))
                    .height(dimensionResource(id = R.dimen._200sdp))
            ) {
                val (closeButton, gridCard) = createRefs()

                Card(modifier = Modifier
                    .constrainAs(gridCard) {
                        linkTo(parent.start, parent.end)
                        linkTo(parent.top, parent.bottom)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }) {
                    QuantityGrid(
                        item?.multiple ?: 0,
                        onQuantitySelect = { quantity ->
                            openAlertDialog = false
                            currentValue = quantity
                            Log.d("Dialog", "$quantity")
                        })
                }
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .clickable { openAlertDialog = false }
                        .constrainAs(closeButton) {
                            linkTo(parent.top, parent.top)
                            linkTo(parent.end, parent.end)
                        }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "close dialog")
                }
            }
        }


    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = "${item?.name?.toCamelCase()}",
            fontFamily = AppFont,
            fontWeight = FontWeight.Normal,
            color = FontPrimaryDark,
            modifier = Modifier.weight(1f),
            fontSize = fontDimensionResource(id = com.intuit.ssp.R.dimen._18ssp)
        )
        Card(
            shape = RoundedCornerShape(dimensionResource(id = com.intuit.sdp.R.dimen._6sdp)),
            border = BorderStroke(1.dp, color = Primary),
            modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._24sdp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (currentValue > 0) {
                            currentValue -= item?.multiple ?: 0
                        }
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    fontFamily = AppFont,
                    fontWeight = FontWeight.Medium,
                    color = Primary,
                    fontSize = fontDimensionResource(id = com.intuit.ssp.R.dimen._18ssp)
                )
            }


        }


        Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._4sdp)))

        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = dimensionResource(id = com.intuit.sdp.R.dimen._24sdp))
                .clickable {
                    openAlertDialog = true
                }, contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentValue.toString(),
                fontFamily = AppFont,
                fontWeight = FontWeight.Normal,
                color = FontPrimaryDark,
                fontSize = fontDimensionResource(id = com.intuit.ssp.R.dimen._18ssp)
            )
        }

        Spacer(modifier = Modifier.width(dimensionResource(id = com.intuit.sdp.R.dimen._4sdp)))


        Card(
            shape = RoundedCornerShape(dimensionResource(id = com.intuit.sdp.R.dimen._6sdp)),
            border = BorderStroke(1.dp, color = Primary),
            modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._24sdp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if ((totalPrice?:0) <= (max?:0))
                            currentValue += item?.multiple ?: 0
                        else
                            showToast(context,"You have reached the cart limit")
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontFamily = AppFont,
                    fontWeight = FontWeight.Medium,
                    color = Primary,
                    fontSize = fontDimensionResource(id = com.intuit.ssp.R.dimen._18ssp)
                )
            }


        }
    }


}