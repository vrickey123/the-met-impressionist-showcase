package com.vrickey123.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        if (title != null && message != null) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = message,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(title = "Title", message = "Hello World")
}