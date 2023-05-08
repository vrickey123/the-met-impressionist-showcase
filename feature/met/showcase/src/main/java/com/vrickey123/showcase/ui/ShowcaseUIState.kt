package com.vrickey123.showcase.ui

import com.vrickey123.met_api.MetObject
import com.vrickey123.state.UIState

data class ShowcaseUIState(
    override val data: List<MetObject> = emptyList(),
    override val loading: Boolean = false,
    override val error: Throwable? = null
): UIState {
    val isEmpty: Boolean
        get() = data.isEmpty()
    override val hasPartialData: Boolean
        get() = super.hasPartialData.and(data.isNotEmpty())
}
