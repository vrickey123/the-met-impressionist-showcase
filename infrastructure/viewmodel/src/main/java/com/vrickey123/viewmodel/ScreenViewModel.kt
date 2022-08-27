package com.vrickey123.viewmodel

import com.vrickey123.model.state.UIState
import kotlinx.coroutines.flow.StateFlow

interface ScreenViewModel<T: UIState> {
    val state: StateFlow<T>
}