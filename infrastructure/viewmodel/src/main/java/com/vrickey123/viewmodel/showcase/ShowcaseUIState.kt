package com.vrickey123.viewmodel.showcase

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.state.UIState

data class ShowcaseUIState(
    override val data: List<MetObject> = emptyList(),
    override val loading: Boolean = false,
    override val error: Throwable? = null
): UIState
