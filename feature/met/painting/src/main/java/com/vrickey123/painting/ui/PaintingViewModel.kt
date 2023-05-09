package com.vrickey123.painting.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrickey123.met_api.MetObject
import com.vrickey123.network.MetRepository
import com.vrickey123.network.di.MetRepoImpl
import com.vrickey123.reducer.Reducer
import com.vrickey123.viewmodel.ScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PaintingViewModel @Inject constructor(
    @MetRepoImpl val metRepository: MetRepository
) : ViewModel(), ScreenViewModel<PaintingUIState>, Reducer<PaintingUIState, MetObject> {

    companion object {
        val TAG by lazy { PaintingViewModel::class.java.simpleName }
    }

    // The runtime value of an objectID used to make an API call for a Painting
    private val objectID: MutableStateFlow<Int?> = MutableStateFlow(null)

    // Reducer
    override val mutableState: MutableStateFlow<PaintingUIState> =
        MutableStateFlow(PaintingUIState(loading = true))

    // Hot Flow of all Result<MetObject> from the database. Emits on all changes to DB.
    override val stream: Flow<Result<MetObject>> = objectID.flatMapLatest {
        if (it != null) {
            metRepository.getMetObject(it)
        } else {
            throw IllegalStateException("null MetObject.id")
        }
    }.catch { Result.failure<Throwable>(it) }

    // ScreenViewModel
    // Combines state of our network requests (mutableState) and database stream
    override val state: StateFlow<PaintingUIState> =
        mutableState.combine(stream) { oldState, streamResult ->
            reduce(oldState, streamResult)
        }.catch {
            reduce(mutableState.value, Result.failure(it))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PaintingUIState(loading = true)
        )

    /**
     * Makes a one-shot API call to [MetRepository.fetchMetObject] to fetch latest [MetObject]
     * from the server.
     *
     * Note: for this sample app, this is unused because we already have the latest [MetObject] in
     * local storage. However, this type of architecture would be used if we needed to fetch the
     * latest content, such as an Article, from the remote using an id.
     * */
    fun fetchPainting(id: Int) = viewModelScope.launch {
        Log.d(TAG, "Fetch painting: $id")
        emitLoading {
            val result = metRepository.fetchMetObject(id)
            result.onFailure { emitError(it) }
        }
    }

    // Reducer
    override fun reduce(oldState: PaintingUIState, result: Result<MetObject>): PaintingUIState {
        return result.fold(
            onSuccess = { PaintingUIState(data = result.getOrThrow()) },
            onFailure = { PaintingUIState(data = oldState.data, error = result.exceptionOrNull()) }
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
        Log.e(TAG, "Emit loading")
        mutableState.update { it.copy(loading = true) }
        action.invoke()
        mutableState.update { it.copy(loading = false) }
    }

    fun setObjectID(id: Int) = objectID.update { id }
}