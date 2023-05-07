package com.vrickey123.painting.ui

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
class PaintingViewModel @Inject constructor(
    @MetRepoImpl val metRepository: MetRepository
) : ViewModel(), ScreenViewModel<PaintingUIState>, Reducer<PaintingUIState, MetObject> {

    companion object {
        val TAG by lazy { PaintingViewModel::class.java.simpleName }
    }

    var objectID: Int? = null;

    override val mutableState: MutableStateFlow<PaintingUIState> =
        MutableStateFlow(PaintingUIState(loading = true))

    // Hot Flow of all Result<PaintingUIState> from the database. Emits on all changes to DB.
    private val stream: StateFlow<PaintingUIState> = metRepository.getMetObject(objectID)
        .map { reduce(it) }
        .catch { reduce(Result.failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PaintingUIState(loading = true)
        )

    override val state: StateFlow<PaintingUIState> =
        mutableState.combine(stream) { mutableState, stream ->
            // If there is a request loading, return the loading mutableState.
            // If there is a request error, return the error mutableState with any prior data from
            // the database stream. (i.e. successfully loaded from DB, then got malformed API response).
            // Otherwise default to the data stream results.
            mutableState.copy(data = stream.data)
        }.catch {
            reduce(Result.failure(it))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PaintingUIState(loading = true)
        )

    fun fetchPainting(id: Int) = viewModelScope.launch {
        Log.d(TAG, "Fetch painting: $id")
        emitLoading {
            val result = metRepository.fetchMetObject(id)
            result.onFailure { emitError(it) }
        }
    }

    override fun reduce(result: Result<MetObject>): PaintingUIState {
        return if (result.isSuccess) {
            PaintingUIState(data = result.getOrThrow())
        } else {
            PaintingUIState(error = result.exceptionOrNull())
        }
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