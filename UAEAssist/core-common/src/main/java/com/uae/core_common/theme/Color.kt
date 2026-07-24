package com.uae.core_common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val theme_color_1 = Color(0xFF252e6d)
val theme_color_2 = Color(0xFFc7425b)
val theme_color_3 = Color(0xFFef8e4a)
val theme_color_4 = Color(0xFFFFD1B4)
val theme_color_5 = Color(0xFFFFE798)
val theme_color_6 = Color(0xFFFFF0DE)
val theme_color_7 = Color(0xFFFFFAEE)
val theme_color_11 = Color(0xFFFFFCF7)
val theme_color_8 = Color(0xFF9A5326)
val theme_color_9 = Color(0xFFB4612E)
val theme_color_10 = Color(0xFFFF8A04)
val theme_color_12 = Color(0xFF9C5427)
val theme_color_13 = Color(0xFFfff4e7)
val theme_color_14 = Color(0xFFFFE1CE)
val theme_color_15 = Color(0xFFFFEFE8)
val theme_color_16 = Color(0xFFFFEADD)
val theme_color_17 = Color(0xFFCA6C00)
val theme_color_18 = Color(0xFFFBDBC6)
val theme_color_19 = Color(0xFFFFF7EE)
val theme_color_20 = Color(0xFFfbf7f4)
val theme_color_21 = Color(0xFFFFF2E8)
val theme_color_22 = Color(0xFFFFEAD3)
val theme_color_23 = Color(0xFFFAECE2)
val theme_color_24 = Color(0xFFfff3ec)
val table_heading_color = Color(0xFFeccfbd)
val theme_color_25 = Color(0xFFF9F3EF)
val theme_color_26 = Color(0xFFFAD4BC)
val theme_color_27 = Color(0xFFFFF5EE)
val theme_color_28 = Color(0xFFFFE0CD)
val theme_color_29 = Color(0xFFFFF4EB)
val theme_color_30 = Color(0xFFFFEFDD)
val theme_color_31 = Color(0xFFEAD2C3)
val theme_color_32 = Color(0xFFf3e9e2)
val theme_color_33 = Color(0xFFF3E9E3)
val theme_color_34 = Color(0xFFCFAF9B)
val theme_color_35 = Color(0xFFf4f0ee)
val theme_color_36 = Color(0xFFede3dd)
val theme_color_37 = Color(0xFFFAE1D0)

 val gray_light_2 = Color(0xFFf5f5f5)
val gray_light_3 = Color(0xFFF4F4F4)
val hint_color = Color(0xFFB3B3B3)
val hint_color_referral = Color(0xFF986F3F)
val yellow_light = Color(0xFFFEECB2)
val yellow_light_2 = Color(0xFFE6CC63)
val yellow_light_3 = Color(0xFFFFF9E7)

val dark_blue = Color(0xFF292D32)
val grey_1 = Color(0xFFc5c5c7)
val grey_2 = Color(0xFFF0EBEB)
val grey_3 = Color(0xFFF4F4F4)
val grey_7 = Color(0xFFeeeef0)
val grey_8 = Color(0xFFf3f2f8)
val green_2 = Color(0xFF72D748)
val green_3 = Color(0xFFC7E8B9)
val green_4 = Color(0xFF0FAE27)
val green_5 = Color(0xFFDCFCD0)
val green_6 = Color(0xFF75D153)
val green_7 = Color(0xFFDDF6EC)
val green_8 = Color(0xFF4C9E2A)
val green_9 = Color(0xFF0D992F)
val red = Color(0xFFFF4D4D)
val red_2 = Color(0xFFFFECE9)
val red_3 = Color(0xFFFB4C31)
val red_4 = Color(0xFFE74C3C)
val red_5 = Color(0xFFF14421)
val red_6 = Color(0xFFFFEDF0)
val light_blue = Color(0xFFf1f5ff)
val blue_sky = Color(0xFF88c4ff)
val blue_sky_2 = Color(0xFFebf3ff)
val bg_color_1 = Color(0xFFf8f8f8)
val bg_color_2 = Color(0xFFFFFBF8)
val bg_color_3 = Color(0xFFF9F9F9)
val divider_color_1 = Color(0xFFF0F0F0)
val divider_color_2 = Color(0xFF2E2E2E)
val divider_color_3 = Color(0xFF616161)
val divider_color_4 = Color(0xFFE8E8E8)
val linear_progress_bar_track_color = Color(0xFFF2F2F4)
val grey_4 = Color(0xFF555555)
val grey_5 = Color(0xFF9A9A9A)
val grey_6 = Color(0xFF585858)
val purple = Color(0xFF8142E9)
val dark_purple = Color(0xFF0b0d1d)


@Composable
fun brush2(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(
            theme_color_1,
            theme_color_7,
            theme_color_7,
            theme_color_2
        )
    )

}

@Composable
fun brush3() : Brush{
    return Brush.verticalGradient(colors = listOf(Color.White, Color.White, theme_color_24))
}
@Composable
fun brush4() : Brush{
    return Brush.horizontalGradient(colors = listOf(theme_color_2, theme_color_3))
}

@Composable
fun astakvargaHeaderBg() : Brush{
    return Brush.verticalGradient(colors = listOf(Color.White, Color.White, table_heading_color))
}
@Composable
fun brush5() : Brush{
    return Brush.horizontalGradient(colors = listOf(theme_color_10.copy(0.1f), theme_color_10.copy(0.3f), theme_color_10, theme_color_10.copy(0.3f), theme_color_10.copy(0.1f)))
}
@Composable
fun brush6() : Brush{
    return Brush.horizontalGradient(colors = listOf(theme_color_12, Color.Black))
}