package com.vrickey123.router

// As of Aug 2022, navigation in compose is still immature and lacks a good URI spec.
sealed class Route(internal val route: String) {

    interface HasID {
        fun getQualifiedPath(objectID: String): String
    }

    sealed class NavGraph(route: String) : Route(route) {
        companion object {
            const val ROOT_SHOWCASE_GRAPH = "showcaseGraph"
            const val ROOT_PAINTING_GRAPH = "paintingGraph"
        }

        object Showcase: NavGraph(ROOT_SHOWCASE_GRAPH)
        object Painting: NavGraph(QueryParameter.create(ROOT_PAINTING_GRAPH, QueryParameter.ID)), HasID {
            override fun getQualifiedPath(objectID: String): String {
                return QueryParameter.replace(route, QueryParameter.ID, objectID)
            }
        }
    }

    sealed class Screen(route: String) : Route(route) {
        companion object {
            const val ROOT_SHOWCASE_SCREEN = "showcaseScreen"
            const val ROOT_PAINTING_SCREEN = "paintingScreen"
        }

        object Showcase : NavGraph(ROOT_SHOWCASE_SCREEN)
        object Painting : NavGraph(QueryParameter.create(ROOT_PAINTING_SCREEN, QueryParameter.ID)), HasID {
            override fun getQualifiedPath(objectID: String): String {
                return QueryParameter.replace(route, QueryParameter.ID, objectID)
            }
        }
    }
}
