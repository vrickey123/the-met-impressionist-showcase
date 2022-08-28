package com.vrickey123.painting.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        Column(modifier.verticalScroll(state = rememberScrollState())) {
            AsyncImageComponent(
                imageData = ImageData(
                    url = paintingUIState.uiStateData.primaryImageSmall,
                    contentDescription = paintingUIState.uiStateData.title
                )
            )
            AboutPaintingCard(
                title = paintingUIState.uiStateData.title,
                artistDisplayName = paintingUIState.uiStateData.artistDisplayName,
                artistNationality = paintingUIState.uiStateData.artistNationality,
                artistDisplayBio = paintingUIState.uiStateData.artistDisplayBio,
                department = paintingUIState.uiStateData.department,
                galleryNumber = paintingUIState.uiStateData.GalleryNumber,
                medium = paintingUIState.uiStateData.medium,
                dimensions = paintingUIState.uiStateData.dimensions
            )
        }
    }
}