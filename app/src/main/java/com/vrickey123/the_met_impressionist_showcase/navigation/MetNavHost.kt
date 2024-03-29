package com.vrickey123.the_met_impressionist_showcase.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.vrickey123.met_route.MetRoute
import com.vrickey123.router.Router

// https://developer.android.com/jetpack/compose/navigation#nested-nav
@Composable
fun MetNavHost(router: Router, snackbarHostState: SnackbarHostState) {
    NavHost(
        navController = router.navHostController,
        startDestination = MetRoute.NavGraph.Showcase.route
    ) {
        showcaseNavGraph(router, snackbarHostState)
        paintingNavGraph(router, snackbarHostState)
    }
}