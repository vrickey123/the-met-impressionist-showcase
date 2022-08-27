package com.vrickey123.viewmodel

interface UIState {
    val data: Any?
    val loading: Boolean
    val error: Throwable?
}