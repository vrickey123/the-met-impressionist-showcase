package com.vrickey123.ui_component.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vrickey123.model.ImageData

@Composable
fun AsyncImageComponent(
    imageData: ImageData,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        modifier = Modifier.fillMaxWidth(),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageData.url)
            .crossfade(true)
            .build(),
        contentDescription = imageData.contentDescription,
        contentScale = contentScale,
    )
}