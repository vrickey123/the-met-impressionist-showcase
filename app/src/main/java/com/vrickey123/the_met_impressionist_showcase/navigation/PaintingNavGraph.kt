package com.vrickey123.the_met_impressionist_showcase.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.vrickey123.met_route.MetRoute
import com.vrickey123.painting.ui.PaintingScreen
import com.vrickey123.painting.ui.PaintingViewModel
import com.vrickey123.router.Router
import com.vrickey123.router.uri.ID

fun NavGraphBuilder.paintingNavGraph(
    router: Router
) {
    navigation(
        startDestination = MetRoute.Screen.Painting.route,
        route = MetRoute.NavGraph.Painting.route
    ) {
        composable(
            route = MetRoute.Screen.Painting.route,
            arguments = listOf(navArgument(ID.key) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                router.navHostController.getBackStackEntry(MetRoute.NavGraph.Painting.route)
            }
            val objectID = navBackStackEntry.arguments?.getString(ID.key)
            val paintingViewModel = hiltViewModel<PaintingViewModel>(parentEntry)
            LaunchedEffect(key1 = objectID) {
                // Unfortunately, objectID is a runtime value and hiltViewModel injects compile-time
                if (objectID != null) {
                    paintingViewModel.objectID = objectID.toInt()
                    paintingViewModel.fetchPainting(objectID.toInt())
                }
            }
            PaintingScreen(paintingViewModel = paintingViewModel, router = router)
        }
    }
}