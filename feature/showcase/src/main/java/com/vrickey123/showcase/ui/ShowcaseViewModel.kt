package com.vrickey123.showcase.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrickey123.model.api.MetObject
import com.vrickey123.network.MetRepository
import com.vrickey123.network.di.MetRepoImpl
import com.vrickey123.reducer.Reducer
import com.vrickey123.viewmodel.ScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor(
    @MetRepoImpl val metRepository: MetRepository
) : ViewModel(), ScreenViewModel<ShowcaseUIState>, Reducer<ShowcaseUIState, List<MetObject>> {

    companion object {
        val TAG by lazy { ShowcaseViewModel::class.java.simpleName }
        const val QUERY_IMPRESSIONISM = "impressionism"
        val TAGS = listOf<String>("impressionism")
    }

    // Reducer
    // Mutable state of all requests initiated from the ViewModel; i.e. fetchMetObject() emits
    // loading and can emit an error.
    override val mutableState: MutableStateFlow<ShowcaseUIState> =
        MutableStateFlow(ShowcaseUIState(loading = true))

    // Hot Flow of all Result<List<MetObject> from the database. Emits on all changes to DB.
    private val stream: Flow<Result<List<MetObject>>> = metRepository.getAllMetObjects()

    // ScreenViewModel
    // Combines state of our network requests (mutableState) and database stream
    override val state: StateFlow<ShowcaseUIState> =
        mutableState.combine(stream) { oldState, streamResult ->
            reduce(oldState, streamResult)
        }.catch {
            reduce(mutableState.value, Result.failure(it))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ShowcaseUIState(loading = true)
        )

    init {
        viewModelScope.launch {
            Log.d(TAG, "Init: get Local Then Remote Met Objects")
            emitLoading {
                val result = metRepository.getLocalThenRemoteMetObjects(QUERY_IMPRESSIONISM, TAGS)
                result.onFailure { emitError(it) }
            }
        }
    }

    /**
     * Makes a one-shot API call to [MetRepository.fetchMetObjects] to fetch latest [MetObject]'s
     * from the sever.
     *
     * Note: for this sample app, this is unused because we already have the latest [MetObject]'s in
     * local storage. However, this type of architecture would be used if we needed to fetch the
     * latest content on every app launch.
     * */
    fun fetchPaintings(ids: List<Int>) = viewModelScope.launch {
        Log.d(TAG, "Fetch showcase")
        emitLoading {
            val result = metRepository.fetchMetObjects(ids)
            result.onFailure { emitError(it) }
        }
    }

    // Reducer
    override fun reduce(oldState: ShowcaseUIState, result: Result<List<MetObject>>): ShowcaseUIState {
        return result.fold(
            onSuccess = { ShowcaseUIState(data = result.getOrDefault(emptyList())) },
            onFailure = { ShowcaseUIState(data = oldState.data, error = result.exceptionOrNull()) }
        )
    }

    // Reducer
    override fun emitError(e: Throwable) {
        viewModelScope.launch {
            Log.d(TAG, "Emit error: $e")
            mutableState.update { it.copy(error = e) }
        }
    }

    // Reducer
    override suspend fun emitLoading(action: suspend () -> Unit) {
        Log.d(TAG, "Emit loading")
        mutableState.update { it.copy(loading = true) }
        action.invoke()
        mutableState.update { it.copy(loading = false) }
    }
}
