package com.vrickey123.reducer

import com.vrickey123.state.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The [Reducer] transforms an old [UIState] and [Result]<[D: Any]>, such as the Result of an API
 * call that returns a data class, and into a new [UIState].
 *
 * Project Note: this interface could be implemented in a concrete `class FooBarReducer` or simply
 * in a ViewModel (as done in this project).
 *
 * @param T: The [UIState] subclass
 * @param D: The data type to be transformed into a [UIState]. It is encapsulated in [UIState.data]
 * and is non-null when [Result.isSuccess].
 *
 * */
interface Reducer<T: UIState, D: Any> {

    /**
     * Mutable [UIState] of all requests initiated from the ViewModel. Emits a new [UIState] on a
     * change.
     * */
    val mutableState: MutableStateFlow<T>

    /**
     * Hot [Flow] of all [Result]<[D]> emitted from the data store. Emits on all changes to [D] in
     * the underlying datastore.
     * */
    val stream: Flow<Result<D>>

    /**
     * Create a new [UIState] from the [oldState] and a [Result] of a network or datastore operation.
     * */
    fun reduce(oldState: T, result: Result<D>): T

    fun emitError(e: Throwable)

    suspend fun emitLoading(action: suspend () -> Unit)
}
