package com.vrickey123.model.state

interface UIState {
    val data: Any?
    val loading: Boolean
    val error: Throwable?
}