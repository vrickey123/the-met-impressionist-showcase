package com.vrickey123.showcase.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrickey123.model.api.MetObject
import com.vrickey123.network.MetRepository
import com.vrickey123.network.di.MetRepoImpl
import com.vrickey123.reducer.Reducer
import com.vrickey123.viewmodel.showcase.ShowcaseUIState
import com.vrickey123.viewmodel.showcase.ShowcaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModelImpl @Inject constructor(
    @MetRepoImpl override val metRepository: MetRepository
) : ViewModel(), ShowcaseViewModel, Reducer<ShowcaseUIState, List<MetObject>> {

    override val mutableState: MutableStateFlow<ShowcaseUIState> =
        MutableStateFlow(ShowcaseUIState(loading = true))

    override val state: StateFlow<ShowcaseUIState>
        get() = mutableState

    init {
        getPaintings()
    }

    override fun getPaintings() {
        flow { emit(metRepository.getLocalThenRemoteMetObjects()) }
            .onEach { mutableState.emit(reduce(it)) }
            .catch { mutableState.emit(reduce(Result.failure(it))) }
            .launchIn(viewModelScope)
    }

    override fun reduce(result: Result<List<MetObject>>): ShowcaseUIState {
        return if (result.isSuccess) {
            ShowcaseUIState(uiStateData = result.getOrDefault(emptyList()))
        } else {
            ShowcaseUIState(error = result.exceptionOrNull())
        }
    }
}
