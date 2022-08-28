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
import com.vrickey123.painting.ui.PaintingViewModelImpl
import com.vrickey123.router.QueryParameter
import com.vrickey123.router.Route
import com.vrickey123.router.Router

fun NavGraphBuilder.paintingNavGraph(
    router: Router
) {
    navigation(
        startDestination = Route.Screen.Showcase.route,
        route = Route.NavGraph.Showcase.route
    ) {
        composable(
            route = Route.Screen.Showcase.route,
            arguments = listOf(navArgument(QueryParameter.ID.key) { type = NavType.IntType })
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                router.navHostController.getBackStackEntry(Route.NavGraph.Showcase.route)
            }
            val objectID = navBackStackEntry.arguments?.getInt(QueryParameter.ID.key)
            val paintingViewModel = hiltViewModel<PaintingViewModelImpl>(parentEntry)
            LaunchedEffect(key1 = objectID) {
                // Unfortunately, objectID is a runtime value and hiltViewModel injects compile-time
                if (objectID != null) paintingViewModel.getPainting(objectID)
            }
            PaintingScreen(paintingViewModel = paintingViewModel, router = router)
        }
    }
}