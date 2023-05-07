package com.vrickey123.showcase.ui

import com.vrickey123.met_api.MetObject

data class ShowcaseUIState(
    override val data: List<com.vrickey123.met_api.MetObject> = emptyList(),
    override val loading: Boolean = false,
    override val error: Throwable? = null
): com.vrickey123.state.UIState
