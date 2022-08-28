package com.vrickey123.showcase.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
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

    companion object {
        const val QUERY = "impressionism"
        val TAGS = listOf<String>("impressionism")
    }

    override val mutableState: MutableStateFlow<ShowcaseUIState> =
        MutableStateFlow(ShowcaseUIState(loading = true))

    override val state: StateFlow<ShowcaseUIState>
        get() = mutableState

    init {
        getPaintings()
    }

    override fun getPaintings() {
        flow<Result<MetSearchResult>> { metRepository.search(QUERY, true, TAGS) }
            .map { metSearchResult ->
                // make a fetchMetObject API call for each objectID in the MetSearchResult
                metSearchResult.getOrThrow().objectIDs.map { metRepository.fetchMetObject(it) }
            }
            .buffer() // creates a buffer of a List<Result<MetObject>> for previous api calls above
            .map { listOfResult ->
                // transform a List<Result<MetObject> to a List<MetObject>
                listOfResult.map { it.getOrThrow() }
            }
            .onEach { mutableState.emit(reduce(Result.success(it))) }
            .catch { mutableState.emit(ShowcaseUIState(error = Throwable("Flow error"))) }
            .launchIn(viewModelScope)
    }

    override fun reduce(result: Result<List<MetObject>>): ShowcaseUIState {
        return if (result.isSuccess) {
            ShowcaseUIState(data = result.getOrDefault(emptyList()))
        } else {
            ShowcaseUIState(error = result.exceptionOrNull())
        }
    }
}
