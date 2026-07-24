package com.uae.core_common.utils

import com.uae.core_common.R
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.theme.theme_color_12
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AsyncImageWithLoader(image: Any?, modifier: Modifier = Modifier, placeholder : Painter? = painterResource(R.drawable.uae_logo)) {


    val imagePainter = rememberAsyncImagePainter(
        model = image,
        error = placeholder,
        placeholder = placeholder
    )

    val imageState by imagePainter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        if (imageState is AsyncImagePainter.State.Loading) {
            ContainedLoadingIndicator(
                containerColor = theme_color_1.copy(alpha = 0.3f),
                indicatorColor = theme_color_12,
                modifier = Modifier.size(20.dp)
            )
        }
        if (imageState is AsyncImagePainter.State.Error) {
            Image(
                painter = placeholder ?: painterResource(R.drawable.uae_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

        }
    }
}


@Composable
fun AsyncProfileImageWithLoader(image: Any?, modifier: Modifier = Modifier, placeholder : Int = R.drawable.profile) {

    val imagePainter = rememberAsyncImagePainter(
        model = image,
        error = painterResource(placeholder),
        placeholder = painterResource(placeholder)
    )

    val imageState by imagePainter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "",
            modifier = Modifier.matchParentSize().clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        if (imageState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                strokeWidth = 5.dp, modifier = Modifier.size(20.dp)
            )
        }
        if (imageState is AsyncImagePainter.State.Error) {
            Image(
                painter = painterResource(placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
