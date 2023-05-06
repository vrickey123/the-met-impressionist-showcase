package com.vrickey123.image.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vrickey123.image.data.ImageData

@Composable
fun AsyncImageComponent(
    modifier: Modifier = Modifier,
    imageData: ImageData,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        modifier = modifier.fillMaxWidth(),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageData.url)
            .crossfade(true)
            .diskCacheKey(imageData.url)
            .memoryCacheKey(imageData.url)
            .build(),
        contentDescription = imageData.contentDescription,
        contentScale = contentScale,
    )
}