package com.vrickey123.state

interface UIState {
    val data: Any?
    val loading: Boolean
    val error: Throwable?
}