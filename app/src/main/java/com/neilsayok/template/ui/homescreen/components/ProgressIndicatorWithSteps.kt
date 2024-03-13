package com.neilsayok.template.ui.homescreen.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.neilsayok.template.R
import com.neilsayok.template.data.model.Point
import com.neilsayok.template.theme.AppFont
import com.neilsayok.template.theme.Error
import com.neilsayok.template.theme.Primary
import com.neilsayok.template.theme.PrimaryTransparent
import com.neilsayok.template.theme.Secondary
import com.neilsayok.template.theme.SecondaryVariant
import com.neilsayok.template.theme.Surface
import com.neilsayok.template.utils.showToast

@Preview
@Composable
fun ProgressIndicatorWithStepsPreview(){
    ProgressIndicatorWithSteps(
        min = 0,
        max = 15000,
        currentValue = 2500,
        points = listOf(
            Point(2000),
            Point(12000),
        )
    )
}


@Composable
fun ProgressIndicatorWithSteps(
    min: Int?,
    max : Int?,
    currentValue : Int?,
    points: List<Point?>?) {

    Log.d("currentValue", currentValue.toString())
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val pointConstraintDotTextList = List((points?: emptyList()).size) { Pair(createRef(),createRef()) }
        val (progressBar, minPrice, maxPrice) = createRefs()

        val currentProgress = ((((currentValue?:0)*100)/(max?:1).toFloat())/100F)
        Log.d("currentValue", currentProgress.toString())


        val padding = dimensionResource(id = com.intuit.sdp.R.dimen._4sdp)
//        createHorizontalChain(minPrice, progressBar, maxPrice)
        Text(text = "${stringResource(id = R.string.text_rupee_symbol)} $min",
            fontFamily = AppFont,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(minPrice){
                start.linkTo(parent.start)
                top.linkTo(progressBar.top)
                bottom.linkTo(progressBar.bottom)
            })

        LinearProgressIndicator(
            progress = currentProgress,
            color = Primary,
            backgroundColor = PrimaryTransparent,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp))
                .constrainAs(progressBar) {
                    linkTo(minPrice.end, maxPrice.start, startMargin = padding, endMargin = padding)
                    linkTo(parent.top, parent.bottom)
                    width = Dimension.fillToConstraints
                },
        )

        Text(text = "${stringResource(id = R.string.text_rupee_symbol)} $max",
            fontFamily = AppFont,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(maxPrice){
                end.linkTo(parent.end)
                top.linkTo(progressBar.top)
                bottom.linkTo(progressBar.bottom)
            })


        pointConstraintDotTextList.forEachIndexed { i, item ->
            val bias = ((((points?.get(i)?.value?:0)*100)/(max?:0).toFloat())/100F)

            Card(
                shape = CircleShape,
                backgroundColor = SecondaryVariant,
                border = BorderStroke(dimensionResource(id = com.intuit.sdp.R.dimen._1sdp), color = Surface),
                modifier = Modifier
                    .size(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp))
                    .constrainAs(item.first) {
                        linkTo(parent.top, parent.bottom)
                        linkTo(progressBar.start, progressBar.end, bias = bias)
                    }

                ) {

            }
            Text(text = "${stringResource(id = R.string.text_rupee_symbol)} ${points?.get(i)?.value}",
                fontFamily = AppFont,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.constrainAs(item.second){
                    start.linkTo(item.first.start)
                    end.linkTo(item.first.start)
                    top.linkTo(item.first.bottom)
                })
        }


    }


}