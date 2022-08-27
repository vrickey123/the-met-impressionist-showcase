package com.vrickey123.viewmodel.showcase

import com.vrickey123.model.api.MetObject
import kotlinx.coroutines.flow.StateFlow

interface ShowcaseViewModel {
    val state: StateFlow<Result<List<MetObject>>>

    fun getPaintings()
}