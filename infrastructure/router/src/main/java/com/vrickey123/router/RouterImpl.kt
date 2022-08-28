package com.vrickey123.router

import androidx.navigation.NavHostController

// https://developer.android.com/jetpack/compose/navigation
class RouterImpl : Router {
    override lateinit var navHostController: NavHostController

    override fun navigate(route: Route.NavGraph.Showcase) {
        navHostController.navigate(route.route)
    }

    override fun navigate(route: Route.NavGraph.Painting, objectID: String) {
        navHostController.navigate(route.route)
    }

    override fun popBackStack() = navHostController.popBackStack()
}