package com.vrickey123.painting.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vrickey123.image.data.ImageData
import com.vrickey123.image.ui.AsyncImageComponent
import com.vrickey123.router.Router
import com.vrickey123.screen.StatefulScreen
import com.vrickey123.ui_component.card.AboutPaintingCard

@Composable
fun PaintingScreen(
    modifier: Modifier = Modifier,
    paintingViewModel: PaintingViewModel,
    router: Router,
    snackbarHostState: SnackbarHostState
) {
    StatefulScreen(
        modifier = modifier,
        screenViewModel = paintingViewModel,
        snackbarHostState = snackbarHostState
    ) { paintingUIState ->
        // https://stackoverflow.com/questions/69394543/fillmaxsize-modifier-not-working-when-combined-with-verticalscroll-in-jetpack-co
        LazyColumn {
            item {
                Column {
                    AsyncImageComponent(
                        imageData = ImageData(
                            url = paintingUIState.data.primaryImageSmall,
                            contentDescription = paintingUIState.data.title
                        )
                    )
                    AboutPaintingCard(
                        modifier = Modifier.fillParentMaxHeight(),
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
    }
}