package com.vrickey123.router

import androidx.navigation.NavHostController
import com.vrickey123.router.uri.Argument
import com.vrickey123.router.uri.Route

class Router {
    lateinit var navHostController: NavHostController

    fun popBackStack(): Boolean = navHostController.popBackStack()

    fun navigate(route: Route) = navHostController.navigate(route.route)

    fun navigate(route: Route.HasArgument, runtimeArgValue: String) {
        navHostController.navigate(route.getQualifiedPath(runtimeArgValue))
    }

    fun navigate(route: Route.HasOptionalArgument, runtimeArgValue: String) {
        navHostController.navigate(route.getQualifiedPath(runtimeArgValue))
    }

    //fun setNavHostController(navHostController: NavHostController)

    //fun getNavHostController(): NavHostController
}