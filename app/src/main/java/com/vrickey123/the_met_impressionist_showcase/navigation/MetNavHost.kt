package com.vrickey123.the_met_impressionist_showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.vrickey123.router.Route
import com.vrickey123.router.Router
import com.vrickey123.showcase.navigation.showcaseNavGraph

// https://developer.android.com/jetpack/compose/navigation#nested-nav
@Composable
fun MetNavHost(router: Router) {
    NavHost(
        navController = router.navHostController,
        startDestination = Route.NavGraph.Showcase.route
    ) {
        showcaseNavGraph(router)
    }
}