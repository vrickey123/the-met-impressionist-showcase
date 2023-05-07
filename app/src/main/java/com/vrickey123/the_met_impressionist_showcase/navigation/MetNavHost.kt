package com.vrickey123.the_met_impressionist_showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.vrickey123.met_route.MetRoute
import com.vrickey123.router.Router

// https://developer.android.com/jetpack/compose/navigation#nested-nav
@Composable
fun MetNavHost(router: Router) {
    NavHost(
        navController = router.navHostController,
        startDestination = MetRoute.Showcase.route
    ) {
        showcaseNavGraph(router)
        paintingNavGraph(router)
    }
}