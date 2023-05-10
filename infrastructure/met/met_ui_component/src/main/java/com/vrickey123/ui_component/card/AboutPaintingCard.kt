package com.vrickey123.ui_component.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 20.dp)) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = String.format("%s by %s", title, artistDisplayName),
                style = MaterialTheme.typography.displaySmall
            )
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
        Text(
            text = stringResource(R.string.label_artist),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W500
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "Nationality: $artistNationality",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = artistDisplayBio,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
internal fun DisplayComponent(
    modifier: Modifier = Modifier,
    department: String,
    galleryNumber: String,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.label_display),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W500
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "Department: $department",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "Gallery Number: $galleryNumber",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
internal fun DetailsComponent(
    modifier: Modifier = Modifier,
    medium: String,
    dimensions: String,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.label_details),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W500
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "Medium: $medium",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "Dimensions: $dimensions",
            style = MaterialTheme.typography.bodyLarge
        )
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
