package com.vrickey123.state

/**
 * A platform interface that models fullscreen UI states as seen by the user, and encapsulates the
 * corresponding data.
 *
 * @property data: The successful state and corresponding data to render the UI.
 * @property loading: The loading state modeled by a Boolean representing an in-progress
 * operation. It can be used to render a loading spinner.
 * @property error: An error state with a Throwable that can be used to render a fullscreen error
 * or message.
 * @property hasPartialData: There is both an error and non-null data.
 * */
interface UIState {
    val data: Any?
    val loading: Boolean
    val error: Throwable?
    val hasPartialData: Boolean
        get() = error != null && data!= null
}