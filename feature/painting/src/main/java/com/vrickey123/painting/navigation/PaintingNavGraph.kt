package com.vrickey123.painting.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.vrickey123.painting.ui.PaintingScreen
import com.vrickey123.painting.ui.PaintingViewModel
import com.vrickey123.router.Path
import com.vrickey123.router.Route
import com.vrickey123.router.Router

fun NavGraphBuilder.paintingNavGraph(
    router: Router
) {
    navigation(
        startDestination = Route.Screen.Painting.route,
        route = Route.NavGraph.Painting.route
    ) {
        composable(
            route = Route.Screen.Painting.route,
            arguments = listOf(navArgument(Path.ID.key) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                router.navHostController.getBackStackEntry(Route.NavGraph.Painting.route)
            }
            val objectID = navBackStackEntry.arguments?.getString(Path.ID.key)
            val paintingViewModel = hiltViewModel<PaintingViewModel>(parentEntry)
            LaunchedEffect(key1 = objectID) {
                // Unfortunately, objectID is a runtime value and hiltViewModel injects compile-time
                if (objectID != null) {
                    paintingViewModel.objectID = objectID
                    paintingViewModel.getPainting(objectID)
                }
            }
            PaintingScreen(paintingViewModel = paintingViewModel, router = router)
        }
    }
}