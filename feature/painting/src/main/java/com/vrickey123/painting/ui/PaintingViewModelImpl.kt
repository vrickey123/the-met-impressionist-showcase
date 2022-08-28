package com.vrickey123.painting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrickey123.model.api.MetObject
import com.vrickey123.network.MetRepository
import com.vrickey123.reducer.Reducer
import com.vrickey123.viewmodel.painting.PaintingUIState
import com.vrickey123.viewmodel.painting.PaintingViewModel
import kotlinx.coroutines.flow.*

class PaintingViewModelImpl(
    override val metRepository: MetRepository
) : ViewModel(),
    PaintingViewModel, Reducer<PaintingUIState, MetObject> {

    override val mutableState: MutableStateFlow<PaintingUIState> =
        MutableStateFlow(PaintingUIState(loading = true))


    override val state: StateFlow<PaintingUIState>
        get() = mutableState

    var objectID: Int = 0

    init {
        // TODO: Pass in or set objectID
        // getPainting()
    }

    override fun getPainting(objectID: Int) {
        metRepository.getMetObject(objectID)
            .onEach { mutableState.emit(reduce(Result.success(it))) }
            .catch { mutableState.emit(reduce(Result.failure(it))) }
            .launchIn(viewModelScope)
    }

    override fun reduce(result: Result<MetObject>): PaintingUIState {
        return if (result.isSuccess) {
            PaintingUIState(data = result.getOrThrow())
        } else {
            PaintingUIState(error = result.exceptionOrNull())
        }
    }
}