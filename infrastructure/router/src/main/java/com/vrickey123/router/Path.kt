package com.vrickey123.router

sealed class Path(val key: String, val argument: String) {

    companion object {
        fun create(root: String, queryParameter: Path): String {
            return root.plus("/")
                .plus(queryParameter.argument)
        }

        fun replace(
            route: String,
            path: Path,
            newValue: String
        ): String {
            return route.replace(path.argument, newValue)
        }
    }

    object ID : Path(key = "id", argument = "{id}")
}
