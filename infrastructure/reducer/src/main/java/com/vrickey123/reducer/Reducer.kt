package com.vrickey123.reducer

import com.vrickey123.model.state.UIState
import kotlinx.coroutines.flow.MutableStateFlow

interface Reducer<T: UIState, D: Any> {
    val mutableState: MutableStateFlow<T>

    fun reduce(result: Result<D>): T
}