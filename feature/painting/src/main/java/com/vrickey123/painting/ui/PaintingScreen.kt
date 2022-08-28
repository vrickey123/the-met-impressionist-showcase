package com.vrickey123.painting.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vrickey123.model.ImageData
import com.vrickey123.router.Router
import com.vrickey123.screen.StatefulScreen
import com.vrickey123.ui_component.card.AboutPaintingCard
import com.vrickey123.ui_component.image.AsyncImageComponent
import com.vrickey123.viewmodel.painting.PaintingViewModel

@Composable
fun PaintingScreen(
    modifier: Modifier = Modifier,
    paintingViewModel: PaintingViewModel,
    router: Router
) {
    StatefulScreen(modifier = modifier, screenViewModel = paintingViewModel) { paintingUIState ->
        Column(modifier) {
            AsyncImageComponent(
                imageData = ImageData(
                    url = paintingUIState.data.primaryImageSmall,
                    contentDescription = paintingUIState.data.title
                )
            )
            AboutPaintingCard(
                title = paintingUIState.data.title,
                artistDisplayName = paintingUIState.data.artistDisplayName,
                artistNationality = paintingUIState.data.artistNationality,
                artistDisplayBio = paintingUIState.data.artistDisplayBio,
                department = paintingUIState.data.department,
                galleryNumber = paintingUIState.data.GalleryNumber,
                medium = paintingUIState.data.medium,
                dimensions = paintingUIState.data.dimensions
            )
        }
    }
}