package com.vrickey123.viewmodel.painting

import com.vrickey123.network.MetRepository
import com.vrickey123.viewmodel.ScreenViewModel

interface PaintingViewModel: ScreenViewModel<PaintingUIState> {
    val metRepository: MetRepository

    fun getPainting(objectID: Int)
}