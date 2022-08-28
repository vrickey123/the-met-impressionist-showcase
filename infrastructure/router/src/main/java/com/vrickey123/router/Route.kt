package com.vrickey123.router

// As of Aug 2022, navigation in compose is still immature and lacks a good URI spec.
sealed class Route(val route: String) {

    interface HasIDArgument {
        fun getQualifiedPath(objectID: String): String
    }

    sealed class NavGraph(route: String) : Route(route) {
        companion object {
            const val ROOT_SHOWCASE_GRAPH = "showcaseGraph"
            const val ROOT_PAINTING_GRAPH = "paintingGraph"
        }

        object Showcase: NavGraph(ROOT_SHOWCASE_GRAPH)
        object Painting: NavGraph(Path.create(ROOT_PAINTING_GRAPH, Path.ID)), HasIDArgument {
            override fun getQualifiedPath(objectID: String): String {
                return Path.replace(route, Path.ID, objectID)
            }
        }
    }

    sealed class Screen(route: String) : Route(route) {
        companion object {
            const val ROOT_SHOWCASE_SCREEN = "showcaseScreen"
            const val ROOT_PAINTING_SCREEN = "paintingScreen"
        }

        object Showcase : NavGraph(ROOT_SHOWCASE_SCREEN)
        object Painting : NavGraph(Path.create(ROOT_PAINTING_SCREEN, Path.ID)), HasIDArgument {
            override fun getQualifiedPath(objectID: String): String {
                return Path.replace(route, Path.ID, objectID)
            }
        }
    }
}
