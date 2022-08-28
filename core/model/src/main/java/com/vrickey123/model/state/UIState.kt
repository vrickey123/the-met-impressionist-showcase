package com.vrickey123.model.state

interface UIState {
    val uiStateData: Any?
    val loading: Boolean
    val error: Throwable?
}