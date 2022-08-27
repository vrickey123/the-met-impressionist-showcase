package com.vrickey123.ui_component.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vrickey123.model.ImageData

@Composable
fun ShowcaseCard(
    modifier: Modifier = Modifier,
    title: String,
    artistDisplayName: String,
    imageData: ImageData,
) {
    ElevatedCard(modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageData.url)
                .crossfade(true)
                .build(),
            contentDescription = imageData.contentDescription,
            contentScale = ContentScale.Fit,
        )
        Column(Modifier.padding(horizontal = 20.dp)) {
            Text(modifier = Modifier.padding(top = 10.dp), text = title)
            Text(modifier = Modifier.padding(vertical = 10.dp), text = artistDisplayName)
        }
    }
}

@Preview
@Composable
fun ShowcaseCardPortraitImagePreview() {
    ShowcaseCard(
        title = "Hello World",
        artistDisplayName = "vrickey123",
        imageData = ImageData(
            url = "https://images.metmuseum.org/CRDImages/ep/web-large/DP341200.jpg",
            contentDescription = "content description"
        )
    )
}

@Preview
@Composable
fun ShowcaseCardLandscapeImagePreview() {
    ShowcaseCard(
        title = "Hello World",
        artistDisplayName = "vrickey123",
        imageData = ImageData(
            url = "https://images.metmuseum.org/CRDImages/ep/web-large/DT1565.jpg",
            contentDescription = "content description"
        )
    )
}
