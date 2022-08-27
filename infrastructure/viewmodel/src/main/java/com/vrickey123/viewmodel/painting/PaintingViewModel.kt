package com.vrickey123.viewmodel.painting

import com.vrickey123.viewmodel.ScreenViewModel

interface PaintingViewModel: ScreenViewModel<PaintingUIState> {
    fun getPainting(objectID: Int)
}