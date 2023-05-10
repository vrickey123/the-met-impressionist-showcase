package com.vrickey123.router.uri

// As of May 2023, navigation in compose is still immature and lacks a good URI spec.
// root = "profile"
abstract class Route(root: String) {
    open val route: String = root

    abstract class HasArgument(
        root: String,
        private val argument: Argument,
        override val route: String = appendPath(root, argument),
    ) : Route(root) {
        companion object {
            fun appendPath(root: String, argument: Argument): String =
                root.plus("/").plus(argument.placeholder)
        }

        // "profile/vrickey123
        fun getQualifiedPath(runtimeArgValue: String): String =
            route.replace(argument.placeholder, runtimeArgValue)
    }

    abstract class HasOptionalArgument(
        root: String,
        private val argument: Argument,
        override val route: String = appendQueryParameter(root, argument)
    ) : Route(root) {

        companion object {
            // "profile?id={id}"
            fun appendQueryParameter(root: String, argument: Argument): String =
                root.plus("?")
                    .plus(argument.key)
                    .plus("=")
                    .plus(argument.placeholder)
        }

        // "profile/vrickey123
        fun getQualifiedPath(runtimeArgValue: String): String =
            route.replace(argument.placeholder, runtimeArgValue)
    }
}
