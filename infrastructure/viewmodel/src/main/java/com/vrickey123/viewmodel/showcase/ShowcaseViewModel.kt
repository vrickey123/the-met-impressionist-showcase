package com.vrickey123.viewmodel.showcase

import com.vrickey123.network.MetRepository
import com.vrickey123.viewmodel.ScreenViewModel

interface ShowcaseViewModel: ScreenViewModel<ShowcaseUIState> {
    val metRepository: MetRepository

    fun getPaintings()
}