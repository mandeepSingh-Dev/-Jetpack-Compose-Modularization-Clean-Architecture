package com.uae.core_common.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zodiaq.ui.theme.Shape_15

@Composable
@Preview
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float = 4f,
    modifier: Modifier = Modifier,
    onRatingChanged: (Float) -> Unit = {}
) {
    val density = LocalDensity.current.density
    val starSize = (18f * density).dp
    val starSpacing = (0.5f * density).dp

    Log.d("dkvndkvnd", starSize.toString())
    Log.d("dkvndkvnd", starSize.toString())

    Row(
        modifier = modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) com.uae.core_common.R.drawable.baseline_star_24 else com.uae.core_common.R.drawable.baseline_star_border_24

            Image(
                painter = painterResource(icon),
                contentDescription = null,
//                tint = iconTintColor,
                modifier = Modifier
                    .clip(Shape_15)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize)
                    .height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}