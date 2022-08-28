package com.vrickey123.router

import androidx.navigation.NavHostController

interface Router {
    var navHostController: NavHostController

    fun navigate(route: Route.NavGraph.Showcase)

    fun navigate(route: Route.NavGraph.Painting, objectID: String)

    fun popBackStack(): Boolean

    //fun setNavHostController(navHostController: NavHostController)

    //fun getNavHostController(): NavHostController
}