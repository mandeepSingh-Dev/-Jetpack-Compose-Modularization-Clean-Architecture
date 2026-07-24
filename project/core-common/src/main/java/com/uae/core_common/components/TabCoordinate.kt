package com.uae.core_common.components

import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.POPPINS_REGULAR
import com.uae.core_common.theme.blue_sky
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zodiaq.ui.theme.Shape_10

import com.zodiaq.ui.theme.Shape_20
import com.uae.core_common.theme.grey_7
import com.uae.core_common.theme.theme_color_16
import com.uae.core_common.theme.theme_color_8
import androidx.compose.foundation.shape.CircleShape


data class TabCoordinate(
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
    val width: Dp = 0.dp,
    val height: Dp = 0.dp
)


@Composable
inline fun <reified T> AnimatedTabs(
    tabs: List<T>,
    selectedTabIndex: Int = 0,
    crossinline labelMapper: @Composable (T) -> String,
    crossinline onSelect: (T, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // stable mutable list for coordinates
    val coordinatesList = remember { mutableStateListOf<TabCoordinate>() }

    // animated values
    val animatedOffsetX by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.offsetX ?: 0.dp,
        animationSpec = tween(200, easing = FastOutSlowInEasing), label = "offsetX"
    )
    val animatedWidth by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.width ?: 0.dp,
        animationSpec = tween(100, easing = FastOutLinearInEasing), label = "width"
    )
    val animatedHeight by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.height ?: 0.dp,
        animationSpec = tween(100, easing = FastOutLinearInEasing), label = "height"
    )
    val animatedOffsetY by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.offsetY ?: 0.dp,
        animationSpec = tween(100, easing = FastOutLinearInEasing), label = "offsetY"
    )
    val density = LocalDensity.current

    Box(modifier = modifier
        .clip(Shape_10)
        .background(color = grey_7, shape = CircleShape)
        .padding()
    ) {
        // Moving indicator
        Box(
            modifier = Modifier
                .offset(x = animatedOffsetX, y = animatedOffsetY)
                .width(animatedWidth)
                .height(animatedHeight)
                .dropShadow(Shape_20, shadow = Shadow(radius = 10.dp, color = blue_sky.copy(alpha = 0.3f)))
                .background(color = blue_sky, shape = CircleShape)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            tabs.forEachIndexed { index, item ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)
                    .onGloballyPositioned { coords ->
                        val offsetX = with(density) { coords.positionInParent().x.toDp() }
                        val offsetY = with(density) { coords.positionInParent().y.toDp() }
                        val width = with(density) { coords.size.width.toDp() }
                        val height = with(density) { coords.size.height.toDp() }

                        if (coordinatesList.size > index) {
                            coordinatesList[index] =
                                TabCoordinate(offsetX, offsetY, width, height)
                        } else {
                            coordinatesList.add(
                                index,
                                TabCoordinate(offsetX, offsetY, width, height)
                            )
                        }
                    }.padding(horizontal = 12.dp, vertical = 14.dp)
                    .clickableAnim {
                        onSelect(item, index)
                    }) {
                    Text(
                        labelMapper(item),
                        fontFamily = if (index == selectedTabIndex) POPPINS_MEDIUM else POPPINS_REGULAR,
                        color = Color.Black,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
@Composable
inline fun <reified T> AnimatedTabs2(
    tabs: List<T>,
    selectedTabIndex: Int = 0,
    crossinline labelMapper: @Composable (T) -> String,
    crossinline onSelect: (T, Int) -> Unit,
    modifier: Modifier = Modifier,
    showIndicator : Boolean = true
) {

    // stable mutable list for coordinates
    val coordinatesList = remember { mutableStateListOf<TabCoordinate>() }

    // animated values
    val animatedOffsetX by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.offsetX ?: 0.dp,
        animationSpec = tween(200, easing = FastOutSlowInEasing), label = "offsetX"
    )
    val animatedWidth by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.width ?: 0.dp,
        animationSpec = tween(100, easing = FastOutLinearInEasing), label = "width"
    )
    val animatedHeight by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.height ?: 0.dp,
        animationSpec = tween(100, easing = FastOutLinearInEasing), label = "height"
    )
    val animatedOffsetY by animateDpAsState(
        targetValue = coordinatesList.getOrNull(selectedTabIndex)?.offsetY ?: 0.dp,
        animationSpec = tween(100, easing = FastOutLinearInEasing), label = "offsetY"
    )
    val density = LocalDensity.current

    Box(modifier = modifier
        .clip(Shape_10)
        .background(color = theme_color_16, shape = Shape_10)
        .padding(5.dp)
    ) {
        // Moving indicator
        if(showIndicator){
            Box(
                modifier = Modifier
                    .offset(x = animatedOffsetX, y = animatedOffsetY)
                    .width(animatedWidth)
                    .height(animatedHeight)
                    .dropShadow(Shape_20, shadow = Shadow(radius = 10.dp, color = theme_color_8.copy(alpha = 0.3f)))
                    .background(color = Color.White, shape = Shape_10)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            tabs.forEachIndexed { index, item ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .onGloballyPositioned { coords ->
                        val offsetX = with(density) { coords.positionInParent().x.toDp() }
                        val offsetY = with(density) { coords.positionInParent().y.toDp() }
                        val width = with(density) { coords.size.width.toDp() }
                        val height = with(density) { coords.size.height.toDp() }

                        if (coordinatesList.size > index) {
                            coordinatesList[index] =
                                TabCoordinate(offsetX, offsetY, width, height)
                        } else {
                            coordinatesList.add(
                                index,
                                TabCoordinate(offsetX, offsetY, width, height)
                            )
                        }
                    }.padding(horizontal = 12.dp, vertical = 10.dp)
                    .clickableAnim {
                        onSelect(item, index)
                    }) {
                    Text(
                        labelMapper(item),
                        fontFamily = if (index == selectedTabIndex) POPPINS_MEDIUM else POPPINS_REGULAR,
                        color = Color.Black,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}