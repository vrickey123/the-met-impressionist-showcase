package com.vrickey123.ui_component.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vrickey123.model.api.MetObject
import com.vrickey123.ui_component.R

@Composable
fun AboutPaintingCard(
    modifier: Modifier = Modifier,
    title: String,
    artistDisplayName: String,
    artistNationality: String,
    artistDisplayBio: String,
    department: String,
    galleryNumber: String,
    medium: String,
    dimensions: String
) {
    Card(modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 20.dp).fillMaxSize()) {
            Text(modifier = Modifier.padding(top = 10.dp), text = String.format("%s by %s", title, artistDisplayName))
            ArtistComponent(Modifier.padding(top = 10.dp), artistNationality, artistDisplayBio)
            DisplayComponent(Modifier.padding(top = 10.dp), department, galleryNumber)
            DetailsComponent(Modifier.padding(vertical = 10.dp), medium, dimensions)
        }
    }
}

@Composable
internal fun ArtistComponent(
    modifier: Modifier = Modifier,
    artistNationality: String,
    artistDisplayBio: String,
) {
    Column(modifier = modifier) {
        Text(text = stringResource(R.string.label_artist))
        Text(modifier = Modifier.padding(top = 10.dp), text = "Nationality: $artistNationality")
        Text(modifier = Modifier.padding(top = 10.dp), text = artistDisplayBio)
    }
}

@Composable
internal fun DisplayComponent(
    modifier: Modifier = Modifier,
    department: String,
    galleryNumber: String,
) {
    Column(modifier) {
        Text(text = stringResource(R.string.label_display))
        Text(modifier = Modifier.padding(top = 10.dp), text = "Department: $department")
        Text(modifier = Modifier.padding(top = 10.dp), text = "Gallery Number: $galleryNumber")
    }
}

@Composable
internal fun DetailsComponent(
    modifier: Modifier = Modifier,
    medium: String,
    dimensions: String,
) {
    Column(modifier) {
        Text(text = stringResource(R.string.label_details))
        Text(modifier = Modifier.padding(top = 10.dp), text = "Medium: $medium")
        Text(modifier = Modifier.padding(top = 10.dp), text = "Dimensions: $dimensions")
    }
}

@Preview
@Composable
fun AboutPaintingCardPreview() {
    AboutPaintingCard(
        title = "Hello World",
        artistDisplayName = "vrickey123",
        artistNationality = "American",
        artistDisplayBio = "A software Engineer who enjoys coding to electronic music",
        department = "Internet",
        galleryNumber = "123",
        medium = "NFT",
        dimensions = "1x1x1"
    )
}
