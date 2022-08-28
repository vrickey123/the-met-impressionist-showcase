package com.vrickey123.showcase.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vrickey123.router.Router
import com.vrickey123.screen.StatefulScreen
import com.vrickey123.viewmodel.showcase.ShowcaseViewModel

@Composable
fun ShowcaseScreen(
    modifier: Modifier = Modifier,
    showcaseViewModel: ShowcaseViewModel,
    router: Router
) {
    StatefulScreen(screenViewModel = showcaseViewModel) {

    }
}