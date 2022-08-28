package com.vrickey123.showcase.ui

import android.util.Log
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
        val IDS = listOf<Int>(
            437133,
            671456,
            436155,
            437654,
            839045,
            482132,
            438816,
            283238,
            437135,
            436965,
            438003,
            437384,
            435621,
            437299,
            436122,
            436964,
            436139,
            438815,
            436950,
            436141,
            436144,
            435702,
            438821,
            437835,
            437310,
            436004,
            436162,
            437984,
            436002,
            436149,
            435991,
            436528,
            436575,
            438016,
            436001,
            438001,
            438820,
            436174,
            435967,
            438015,
            436536,
            437868,
            435875,
            435802,
            435739,
            436095,
            436840,
            436123,
            436534,
            435868,
            436529,
            436530,
            437175,
            437980,
            436951,
            438136,
            488978,
            435880,
            435626,
            436548,
            437640,
            437896,
            436140,
            483301,
            436970,
            437890,
            435881,
            436553
        )
    }

    override val mutableState: MutableStateFlow<ShowcaseUIState> =
        MutableStateFlow(ShowcaseUIState(loading = true))

    override val state: StateFlow<ShowcaseUIState>
        get() = mutableState

    init {
        getPaintings()
    }

    override fun getPaintings() {
        flow<Result<List<MetObject>>> {
            metRepository.search(QUERY, true, TAGS).map { metSearchResult ->
                // make a fetchMetObject API call for each objectID in the MetSearchResult
                metSearchResult.objectIDs.map {
                    // transform a List<Result<MetObject> to a List<MetObject>
                    metRepository.fetchMetObject(it).getOrThrow()
                }
            }
        }.onEach {
            mutableState.emit(reduce(it))
        }.catch {
            mutableState.emit(reduce(Result.failure(it)))
        }.launchIn(viewModelScope)
    }

    override fun reduce(result: Result<List<MetObject>>): ShowcaseUIState {
        return if (result.isSuccess) {
            ShowcaseUIState(data = result.getOrDefault(emptyList()))
        } else {
            ShowcaseUIState(error = result.exceptionOrNull())
        }
    }
}
