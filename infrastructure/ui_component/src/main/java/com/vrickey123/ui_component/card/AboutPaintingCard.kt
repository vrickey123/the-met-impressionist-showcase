package com.vrickey123.ui_component.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 20.dp)) {
            Text(modifier = Modifier.padding(top = 10.dp), text = String.format("%s by %s", title, artistDisplayName))
            ArtistComponent(Modifier.padding(top = 10.dp), artistNationality, artistDisplayBio)
            DisplayComponent(Modifier.padding(top = 10.dp), department, galleryNumber)
            DetailsComponent(Modifier.padding(vertical = 10.dp), medium, dimensions)
        }
    }
}

@Composable
internal fun ColumnScope.ArtistComponent(
    modifier: Modifier = Modifier,
    artistNationality: String,
    artistDisplayBio: String,
) {
    Text(text = stringResource(R.string.label_artist))
    Text(modifier = Modifier.padding(top = 10.dp), text = "Nationality: $artistNationality")
    Text(modifier = Modifier.padding(top = 10.dp), text = artistDisplayBio)
}

@Composable
internal fun ColumnScope.DisplayComponent(
    modifier: Modifier = Modifier,
    department: String,
    galleryNumber: String,
) {
    Text(text = stringResource(R.string.label_display))
    Text(modifier = Modifier.padding(top = 10.dp), text = "Department: $department")
    Text(modifier = Modifier.padding(top = 10.dp), text = "Gallery Number: $galleryNumber")
}

@Composable
internal fun ColumnScope.DetailsComponent(
    modifier: Modifier = Modifier,
    medium: String,
    dimensions: String,
) {
    Text(text = stringResource(R.string.label_details))
    Text(modifier = Modifier.padding(top = 10.dp), text = medium)
    Text(modifier = Modifier.padding(top = 10.dp), text = dimensions)
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
