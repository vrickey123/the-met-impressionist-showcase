package com.vrickey123.router.uri

interface Argument {
    val key: String // "id"
    val placeholder: String // {id}
        get() = String.format("{%s}", key)
}

object ID: Argument {
    override val key: String
        get() = "id"
}