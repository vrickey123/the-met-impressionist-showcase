package com.vrickey123.viewmodel

import com.vrickey123.model.state.UIState
import kotlinx.coroutines.flow.StateFlow

/**
 * The [ScreenViewModel] enforces all Android ViewModels that back a fullscreen UI define a
 * [UIState] with an immutable [StateFlow].
 *
 * @param T: The [UIState] emitted on a change.
 *
 * @property state: An immutable [StateFlow] that emits a [UIState] on a change.
 * */
interface ScreenViewModel<T: UIState> {
    val state: StateFlow<T>
}