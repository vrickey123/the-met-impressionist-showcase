package com.vrickey123.showcase.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vrickey123.router.Route
import com.vrickey123.router.Router
import com.vrickey123.showcase.ui.ShowcaseScreen
import com.vrickey123.showcase.ui.ShowcaseViewModelImpl

fun NavGraphBuilder.showcaseNavGraph(
    router: Router
) {
    navigation(
        startDestination = Route.Screen.Showcase.route,
        route = Route.NavGraph.Showcase.route
    ) {
        composable(
            route = Route.Screen.Showcase.route,
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                router.navHostController.getBackStackEntry(Route.NavGraph.Showcase.route)
            }
            ShowcaseScreen(
                showcaseViewModel = hiltViewModel<ShowcaseViewModelImpl>(parentEntry),
                router = router
            )
        }
    }
}