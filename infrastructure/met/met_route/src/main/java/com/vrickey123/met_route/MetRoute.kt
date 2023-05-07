package com.vrickey123.met_route

import com.vrickey123.router.uri.ID
import com.vrickey123.router.uri.Route

sealed class MetRoute {

    sealed class NavGraph(route: String) : Route(route) {
        companion object {
            private const val ROOT_SHOWCASE_GRAPH = "showcaseGraph"
            private const val ROOT_PAINTING_GRAPH = "paintingGraph"
        }

        object Showcase: Route(root = ROOT_SHOWCASE_GRAPH)

        object Painting: Route.HasArgument(root = ROOT_PAINTING_GRAPH, ID)
    }

    sealed class Screen(route: String) : Route(route) {
        companion object {
            private const val ROOT_SHOWCASE_SCREEN = "showcaseScreen"
            private const val ROOT_PAINTING_SCREEN = "paintingScreen"
        }

        object Showcase: Route(root = ROOT_SHOWCASE_SCREEN)

        object Painting: Route.HasArgument(root = ROOT_PAINTING_SCREEN, ID)
    }
}
