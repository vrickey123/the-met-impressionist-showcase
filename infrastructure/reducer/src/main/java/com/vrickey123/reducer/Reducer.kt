package com.vrickey123.reducer

import com.vrickey123.model.state.UIState
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The [Reducer] is used to take a [Result]<[D]> of [Any] type, such as the Result of an API call
 * that returns a data class, and transform it into a [UIState].
 *
 * The transformation is implemented in the [Reducer.reduce] function, which emits a new [UIState].
 *
 * Project Note: this interface could be implemented in a concrete `class FooBarReducer` or simply
 * in a ViewModel (as done in this project).
 *
 * @param T: The [UIState] subclass
 * @param D: The data type to be transformed into a [UIState]. It is encapsulated in [UIState.data]
 * and is non-null when [Result.isSuccess].
 *
 * @property mutableState: A [MutableStateFlow]<[T]> that emits a new [UIState] on a change.
 *
 * */
interface Reducer<T: UIState, D: Any> {
    val mutableState: MutableStateFlow<T>

    fun reduce(result: Result<D>): T
}