package com.vrickey123.the_met_impressionist_showcase.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vrickey123.met_route.MetRoute
import com.vrickey123.router.Router
import com.vrickey123.showcase.ui.ShowcaseScreen
import com.vrickey123.showcase.ui.ShowcaseViewModel

fun NavGraphBuilder.showcaseNavGraph(
    router: Router
) {
    navigation(
        startDestination = MetRoute.Screen.Showcase.route,
        route = MetRoute.NavGraph.Showcase.route
    ) {
        composable(
            route = MetRoute.Screen.Showcase.route,
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                router.navHostController.getBackStackEntry(MetRoute.NavGraph.Showcase.route)
            }
            ShowcaseScreen(
                showcaseViewModel = hiltViewModel<ShowcaseViewModel>(parentEntry),
                router = router
            )
        }
    }
}