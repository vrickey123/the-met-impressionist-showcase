package com.vrickey123.painting.navigation

import android.util.Log
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
import com.vrickey123.router.Path
import com.vrickey123.router.Route
import com.vrickey123.router.Router

fun NavGraphBuilder.paintingNavGraph(
    router: Router
) {
    navigation(
        startDestination = "paintingScreen/{id}",
        route = "paintingGraph/{id}"
    ) {
        composable(
            route = "paintingScreen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                router.navHostController.getBackStackEntry(Route.NavGraph.Painting.route)
            }
            val objectID = navBackStackEntry.arguments?.getString("id")
            val paintingViewModel = hiltViewModel<PaintingViewModelImpl>(parentEntry)
            LaunchedEffect(key1 = objectID) {
                // Unfortunately, objectID is a runtime value and hiltViewModel injects compile-time
                if (objectID != null) {
                    Log.d("paintingNavGraph", "objectID is present")
                    paintingViewModel.objectID = objectID
                    paintingViewModel.getPainting(objectID)
                } else {
                    Log.e("paintingNavGraph", "objectID is null")
                }
            }
            PaintingScreen(paintingViewModel = paintingViewModel, router = router)
        }
    }
}