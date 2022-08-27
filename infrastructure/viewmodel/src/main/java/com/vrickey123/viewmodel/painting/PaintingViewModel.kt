package com.vrickey123.viewmodel.painting

import com.vrickey123.model.api.MetObject
import kotlinx.coroutines.flow.StateFlow

interface PaintingViewModel {
    val state: StateFlow<Result<MetObject>>
    
    fun getPainting(objectID: Int)
}