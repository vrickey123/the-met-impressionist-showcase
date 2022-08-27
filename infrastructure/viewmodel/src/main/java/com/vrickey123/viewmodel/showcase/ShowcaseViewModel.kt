package com.vrickey123.viewmodel.showcase

import com.vrickey123.viewmodel.ScreenViewModel

interface ShowcaseViewModel: ScreenViewModel<ShowcaseUIState> {
    fun getPaintings()
}