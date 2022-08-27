package com.vrickey123.router

import androidx.navigation.NavHostController

interface Router {
    val navHostController: NavHostController

    fun navigate(route: Route.NavGraph.Showcase)

    fun navigate(route: Route.NavGraph.Painting, objectID: String)

    fun popBackStack(): Boolean
}