package com.vrickey123.painting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrickey123.model.api.MetObject
import com.vrickey123.network.MetRepository
import com.vrickey123.network.di.MetRepoImpl
import com.vrickey123.reducer.Reducer
import com.vrickey123.viewmodel.painting.PaintingUIState
import com.vrickey123.viewmodel.painting.PaintingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PaintingViewModelImpl @Inject constructor(
    @MetRepoImpl override val metRepository: MetRepository
) : ViewModel(), PaintingViewModel, Reducer<PaintingUIState, MetObject> {

    override val mutableState: MutableStateFlow<PaintingUIState> =
        MutableStateFlow(PaintingUIState(loading = true))


    override val state: StateFlow<PaintingUIState>
        get() = mutableState

    override var objectID: String = "";

    init {
        getPainting(objectID)
    }

    override fun getPainting(objectID: String) {
        flow { emit(metRepository.getMetObject(objectID)) }
            .onEach { mutableState.emit(reduce(it)) }
            .catch { mutableState.emit(reduce(Result.failure(it))) }
            .launchIn(viewModelScope)
    }

    override fun reduce(result: Result<MetObject>): PaintingUIState {
        return if (result.isSuccess) {
            PaintingUIState(uiStateData = result.getOrThrow())
        } else {
            PaintingUIState(error = result.exceptionOrNull())
        }
    }
}