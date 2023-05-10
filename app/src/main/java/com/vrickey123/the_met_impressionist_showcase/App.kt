package com.vrickey123.the_met_impressionist_showcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vrickey123.router.Router
import com.vrickey123.the_met_impressionist_showcase.ui.theme.TheMetImpressionistShowcaseTheme
import androidx.navigation.compose.rememberNavController
import com.vrickey123.the_met_impressionist_showcase.navigation.MetNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    router: Router,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    router.navHostController = rememberNavController()
    TheMetImpressionistShowcaseTheme {
        Scaffold(
            topBar = {
                SmallTopAppBar(title = { Text(text = stringResource(R.string.top_app_bar_title)) })
            },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                // Screens are swapped out of the Scaffold Container like Fragments in an Activity
                MetNavHost(router, snackbarHostState)
            }
        }
    }
}