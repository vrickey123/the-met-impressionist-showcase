package com.vrickey123.router

sealed class QueryParameter(val key: String, val argument: String) {

    companion object {
        fun create(root: String, queryParameter: QueryParameter): String {
            return root.plus("/")
                .plus(queryParameter.key)
                .plus("=")
                .plus(queryParameter.argument)
        }

        fun replace(
            route: String,
            queryParameter: QueryParameter,
            newValue: String
        ): String {
            return route.replace(queryParameter.argument, newValue)
        }
    }

    object ID : QueryParameter(key = "id", argument = "{id}")
}
