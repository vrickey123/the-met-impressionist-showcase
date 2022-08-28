package com.vrickey123.router

import androidx.navigation.NavHostController

// https://developer.android.com/jetpack/compose/navigation
class RouterImpl : Router {
    override lateinit var navHostController: NavHostController

    override fun navigate(route: Route.NavGraph.Showcase) {
        navHostController.navigate(route.route)
    }

    override fun navigate(route: Route.NavGraph.Painting, objectID: String) {
        val path = route.getQualifiedPath(objectID)
        navHostController.navigate("paintingGraph/${objectID}")
    }

    override fun popBackStack() = navHostController.popBackStack()
}