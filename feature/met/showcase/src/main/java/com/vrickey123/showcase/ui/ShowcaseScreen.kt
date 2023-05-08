package com.vrickey123.showcase.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrickey123.image.data.ImageData
import com.vrickey123.met_route.MetRoute
import com.vrickey123.router.Router
import com.vrickey123.screen.StatefulScreen
import com.vrickey123.showcase.R
import com.vrickey123.ui_component.card.ShowcaseCard

@Composable
fun ShowcaseScreen(
    modifier: Modifier = Modifier,
    showcaseViewModel: ShowcaseViewModel,
    router: Router,
    snackbarHostState: SnackbarHostState
) {
    StatefulScreen(
        modifier = modifier,
        loadingScreenTitle = stringResource(id = R.string.loading_title),
        loadingScreenMessage = stringResource(id = R.string.loading_message),
        screenViewModel = showcaseViewModel,
        snackbarHostState = snackbarHostState
    ) { showcaseUIState ->
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(
                count = showcaseUIState.data.size,
                key = { index -> showcaseUIState.data[index].objectID },
            ) { i ->
                ShowcaseCard(
                    modifier = Modifier.clickable {
                        router.navigate(
                            route = MetRoute.NavGraph.Painting,
                            runtimeArgValue = showcaseUIState.data[i].objectID.toString()
                        )
                    },
                    title = showcaseUIState.data[i].title,
                    artistDisplayName = showcaseUIState.data[i].artistDisplayName,
                    imageData = ImageData(
                        url = showcaseUIState.data[i].primaryImageSmall,
                        contentDescription = showcaseUIState.data[i].title
                    )
                )
            }
        }
    }
}