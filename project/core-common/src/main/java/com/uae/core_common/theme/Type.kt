package com.uae.core_common.theme

import com.uae.core_common.R
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
val POPPINS_REGULAR = FontFamily(
    Font(R.font.poppins,FontWeight.Normal),
)

val POPPINS_BOLD = FontFamily(
    Font(R.font.poppins_bold,FontWeight.Normal),
)

val POPPINS_MEDIUM = FontFamily(
    Font(R.font.poppins_medium,FontWeight.Normal),
)

val POPPINS_SEMI_BOLD = FontFamily(
    Font(R.font.poppins_semibold,FontWeight.Normal),
)



// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
//        fontFamily = IBM_PLEX_SANS_REGULAR,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        color = Color.Black,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)