package com.uae.core_common.components

import com.uae.core_common.theme.POPPINS_MEDIUM
import com.uae.core_common.theme.POPPINS_REGULAR
import com.uae.core_common.theme.grey_5
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.theme.theme_color_2
import com.uae.core_common.theme.theme_color_3
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20

@Composable
fun GradientButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    @DrawableRes endIcon: Int? = null,
    enabled: Boolean = true,
    isClickable : Boolean = enabled,
    fontSize: TextUnit = TextUnit.Unspecified,
    verticalPadding : Dp = 12.dp,
    horizontalPadding: Dp = 10.dp,
    bgColor : Color = theme_color_2,
    fontColor : Color = Color.White,
    isBorder : Boolean = false,
    isGradient : Boolean = true,
    fontFamily : FontFamily = POPPINS_MEDIUM,
    shape : RoundedCornerShape = Shape_15
) {

    val buttonBackground = if (enabled) {
        Brush.horizontalGradient(
            colors = listOf(
                bgColor,
                if(isGradient) theme_color_3 else bgColor,
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.Gray.copy(alpha = 0.6f),
                Color.Gray.copy(alpha = 0.6f)
            )
        )
    }

    val borderColor = if(enabled) theme_color_3 else grey_5

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                if(enabled || isClickable){
                    modifier.clickableDebAnim {
                        onClick()
                    }
                }else{
                    modifier
                }
            )
            .border(width = if(isBorder) 1.5.dp else 0.dp, color = if(isBorder) borderColor else Color.Transparent, shape = shape)
            .background(
                brush = buttonBackground,
                shape = shape
            )
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            if (icon != null) Image(painter = painterResource(icon), contentDescription = "")
            Text(
                text,
                style = TextStyle(
                    fontFamily = fontFamily,
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = Color.White
                ),
                color = fontColor,
                maxLines = 1,
                minLines = 1,
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                ),
            )
            if (endIcon != null) Image(painter = painterResource(endIcon), contentDescription = "")
        }
    }

}


@Composable
fun OutlineButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    @DrawableRes endIcon: Int? = null,
    enabled: Boolean = true,
    isClickable : Boolean = enabled,
    fontSize: TextUnit = TextUnit.Unspecified,
    verticalPadding : Dp = 12.dp,
    horizontalPadding: Dp = 10.dp,
    bgColor : Color = Color.White,
    fontColor : Color = Color.White,
    isBorder : Boolean = false,
    isGradient : Boolean = true,
    fontFamily : FontFamily = POPPINS_MEDIUM,
    shape : RoundedCornerShape = Shape_15
) {


    val borderColor = if(enabled) theme_color_3 else grey_5

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(
                if(enabled || isClickable){
                    modifier.clickableDebAnim {
                        onClick()
                    }
                }else{
                    modifier
                }
            )
            .border(width = if(isBorder) 1.5.dp else 0.dp, color = if(isBorder) borderColor else Color.Transparent, shape = shape)
            .background(
                color = bgColor,
                shape = shape
            )
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            if (icon != null) Image(painter = painterResource(icon), contentDescription = "")
            Text(
                text,
                style = TextStyle(
                    fontFamily = fontFamily,
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    color = Color.White
                ),
                color = if(enabled) fontColor else grey_5,
                maxLines = 1,
                minLines = 1,
                fontSize = fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                ),
            )
            if (endIcon != null) Image(painter = painterResource(endIcon), contentDescription = "")
        }
    }

}


@Composable
fun GradientCustomButton(
    onClick: () -> Unit,
    text: String,
    textColor: Color = Color.White,
    horizontal: Dp = 25.dp,
    vertical: Dp = 10.dp,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    iconTint : Color = Color.Unspecified,
    fontSize : TextUnit = 11.sp,
    enabled: Boolean = true
) {

    val buttonBackground = if (enabled) {
        Brush.horizontalGradient(
            colors = listOf(
                 theme_color_1,
                 theme_color_1,
                 theme_color_2
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.Gray.copy(alpha = 0.6f),
                Color.Gray.copy(alpha = 0.6f)
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                brush = buttonBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = horizontal, vertical = vertical)
            .then(
                if(enabled){
                    Modifier.clickableDebAnim {
                        onClick()
                    }
                }else {Modifier}
            )

    ) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) Image(painter = painterResource(icon), contentDescription = "", colorFilter = ColorFilter.tint(iconTint))
            Text(
                text,
                style = TextStyle(
                    fontFamily = POPPINS_REGULAR,
                    color = textColor,
                    fontSize = fontSize,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                ),
            )
        }
    }

}

@Composable
fun btn() {
    GradientCustomButton(onClick = {}, text = "dkcdkjcd")
}

@Composable
fun FilledButton(
    onClick: () -> Unit,
    text: String,
    color: Color = theme_color_2,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    @DrawableRes icon: Int? = null,
) {
    Button(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = contentPadding,
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = textColor)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text,
                style = TextStyle(fontFamily = POPPINS_REGULAR, color = textColor),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
        }
    }

}


@Composable
fun FilledCustomButton(
    onClick: () -> Unit,
    text: String,
    fontFamily: FontFamily = POPPINS_MEDIUM,
    fontSize : TextUnit = 12.sp,
    textDecoration: TextDecoration? = null,
    textColor: Color = Color.White,
    textDisabledColor: Color = Color.White,
    horizontal: Dp = 25.dp,
    vertical: Dp = 10.dp,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    textAlign: TextAlign = TextAlign.Unspecified,
    shape: Shape = Shape_20,
    color: Color = theme_color_2,
    disabledColor: Color = Color.Gray.copy(alpha = 0.6f)
) {


    val updateModifier = modifier
        .then(
            if (enabled) Modifier.clickableDebAnim {
                onClick()
            } else Modifier
        )
        .clip(RoundedCornerShape(20.dp))

        .background(
            color = if (enabled) color else disabledColor,
            shape = shape
        )
        .padding(horizontal = horizontal, vertical = vertical)


    Box(
        contentAlignment = Alignment.Center,
        modifier = updateModifier
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            if (icon != null) Image(painter = painterResource(icon), contentDescription = "")
            Text(
                text,
                style = TextStyle(
                    fontFamily = fontFamily,
                    color = if (enabled) textColor else textDisabledColor,
                    fontSize = fontSize,
                    textAlign = textAlign,
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    textDecoration = textDecoration
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(
                        Alignment.CenterVertically
                    )
                    .padding(start = 5.dp),
            )
        }
    }
}


@Composable
fun OutlineCustomButton(
    onClick: () -> Unit,
    text: String,
    fontSize : TextUnit = 11.sp,
    fontFamily: FontFamily = POPPINS_REGULAR,
    textColor: Color = Color.White,
    textDisabledColor: Color = Color.White,
    horizontal: Dp = 10.dp,
    vertical: Dp = 12.dp,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    color: Color = theme_color_2,
    shape : RoundedCornerShape = Shape_20,
    disabledColor: Color = Color.Gray.copy(alpha = 0.6f),
) {

    val updateModifier = modifier
        .border(
            color = if (enabled) color else disabledColor,
            width = 1.dp,
            shape = shape
        )
        .padding(horizontal = horizontal, vertical = vertical)
        .then(
            Modifier.clickableDebAnim {
                onClick()
            }
        )

    Box(
        contentAlignment = Alignment.Center,
        modifier = updateModifier
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            if (icon != null) Image(painter = painterResource(icon), contentDescription = "")
            Text(
                text,
                style = TextStyle(
                    fontFamily = fontFamily,
                    color = if (enabled) textColor else textDisabledColor,
                    fontSize = fontSize,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                ),
            )
        }
    }
}

