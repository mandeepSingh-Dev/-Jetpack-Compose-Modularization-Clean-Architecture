package com.uae.core_common.components

import com.uae.core_common.R
import com.uae.core_common.theme.POPPINS_BOLD
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.theme.theme_color_12
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun HeaderView(
    title: String,
    endContent: @Composable () -> Unit = {},
    onBack  : (() -> Unit) ? = null
) {

    val density = LocalDensity.current
    var iconWidth by rememberSaveable { mutableStateOf(0) }

    var maxWidth by rememberSaveable { mutableStateOf(0) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = theme_color_1,
            )
            .statusBarsPadding()
            .onSizeChanged {
                maxWidth = it.width - iconWidth
                Log.d("dnckdnckd", maxWidth.toString())
            }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        val (backIcon, titleRow) = createRefs()

        Image(
            painter = painterResource(R.drawable.baseline_arrow_back_24),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(backIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clickableDebAnim {
                    if(onBack == null) {
//                        navController?.popBackStack()
                    }else{
                        onBack()
                    }
                }
                .onSizeChanged {
                    iconWidth = it.width
                })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .constrainAs(titleRow) {
                    top.linkTo(backIcon.top)
                    bottom.linkTo(backIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .widthIn(max = with(density) { maxWidth.toDp() }.value.dp.plus(20.dp))
                .padding(start = 10.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            endContent()
        }
    }

}

@Composable
fun HeaderViewBackCallback(
    title: String,
    endContent: @Composable () -> Unit = {},
    onBack  : (() -> Unit) ? = null
) {

    val density = LocalDensity.current
    var iconWidth by rememberSaveable { mutableStateOf(0) }

    var maxWidth by rememberSaveable { mutableStateOf(0) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(theme_color_12)
            .statusBarsPadding()
            .onSizeChanged {
                maxWidth = it.width - iconWidth
                Log.d("dnckdnckd", maxWidth.toString())
            }
    ) {
        val (backIcon, titleRow) = createRefs()

        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(backIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clickableDebAnim {
                        onBack?.invoke()
                }
                .onSizeChanged {
                    iconWidth = it.width
                })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .constrainAs(titleRow) {
                    top.linkTo(backIcon.top)
                    bottom.linkTo(backIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .widthIn(max = with(density) { maxWidth.toDp() }.value.dp.plus(20.dp))
                .padding(start = 10.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(start = 30.dp)
            ) {
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    fontFamily = POPPINS_BOLD,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            endContent()
        }
    }

}
/*

@Composable
fun HeaderHomeView(
    title: String,
    endContent: @Composable () -> Unit = {},
    onClickMenu: () -> Unit
) {

    val density = LocalDensity.current
    var iconWidth by rememberSaveable { mutableStateOf(0) }

    var maxWidth by rememberSaveable { mutableStateOf(0) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                Shape_20,
                shadow = Shadow(radius = 10.dp, spread = 5.dp, color = theme_color_12)
            )
            .background(
                theme_color_12,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .statusBarsPadding()
            .onSizeChanged {
                maxWidth = it.width - iconWidth
                Log.d("dnckdnckd", maxWidth.toString())
            }
    ) {
        val (backIcon, titleRow) = createRefs()

        Image(
            painter = painterResource(R.drawable.menu),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(backIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clickableDebAnim {
                    onClickMenu()
                }
                .onSizeChanged {
                    iconWidth = it.width
                })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .constrainAs(titleRow) {
                    top.linkTo(backIcon.top)
                    bottom.linkTo(backIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .widthIn(max = with(density) { maxWidth.toDp() }.value.dp)
                .padding(horizontal = 10.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    fontFamily = INRIA_BOLD,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            endContent()

        }
    }

}

@Composable
fun HeaderView2(
    title: String,
    endContent: @Composable () -> Unit = {},
    navController: NavController? = null,
) {

    val density = LocalDensity.current
    var iconWidth by rememberSaveable { mutableStateOf(0) }

    var maxWidth by rememberSaveable { mutableStateOf(0) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                Shape_20,
                shadow = Shadow(radius = 10.dp, spread = 5.dp, color = theme_color_12)
            )
            .background(
                theme_color_12,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .statusBarsPadding()
            .onSizeChanged {
                maxWidth = it.width - iconWidth
                Log.d("dnckdnckd", maxWidth.toString())
            }
    ) {
        val (backIcon, titleRow) = createRefs()

        Image(
            painter = painterResource(R.drawable.back_icon),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(backIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clickableDebAnim {
                    navController?.popBackStack()
                }
                .onSizeChanged {
                    iconWidth = it.width
                })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .constrainAs(titleRow) {
                    top.linkTo(backIcon.top)
                    bottom.linkTo(backIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .widthIn(max = with(density) { maxWidth.toDp() }.value.dp)
                .padding(horizontal = 10.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    fontFamily = INRIA_BOLD,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            endContent()

        }
    }


}*/


@Composable
fun HomeHeaderView(onClickMenu : () -> Unit,
                   onClickNotification :  () -> Unit){
    Row(modifier = Modifier.fillMaxWidth()
        .background(color = theme_color_1, shape = androidx.compose.foundation.shape.RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        .statusBarsPadding()
        .padding(horizontal = 15.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)) {

        Image(painterResource(R.drawable.menu), contentDescription = null, modifier = Modifier.clickableDebAnim{
        onClickMenu()
        })
        Box(modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center) {
            Image(painterResource(R.drawable.logo), contentDescription = null, modifier = Modifier.size(45.dp))
        }
        Image(painterResource(R.drawable.notification), contentDescription = null, modifier = Modifier.size(30.dp)
            .clickableDeb{
                onClickNotification()
            })
    }
}