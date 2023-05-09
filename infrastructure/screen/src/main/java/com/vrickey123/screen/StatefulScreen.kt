package com.vrickey123.screen

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.vrickey123.state.UIState
import com.vrickey123.viewmodel.ScreenViewModel

const val TAG_STATEFUL_SCREEN = "StatefulScreen"

@Composable
fun <T : UIState> StatefulScreen(
    modifier: Modifier = Modifier,
    screenViewModel: ScreenViewModel<T>,
    snackbarHostState: SnackbarHostState,
    loadingScreenTitle: String? = null,
    loadingScreenMessage: String? = null,
    success: @Composable (uiState: T) -> Unit
) {
    val uiState by screenViewModel.state.collectAsState()
    when {
        uiState.error != null && !uiState.hasPartialData -> {
            Log.d(TAG_STATEFUL_SCREEN, "Error | UIState: $uiState")
            ErrorScreen(modifier, uiState.error)
        }
        uiState.loading -> {
            Log.d(TAG_STATEFUL_SCREEN, "Loading | UIState: $uiState")
            LoadingScreen(modifier, loadingScreenTitle, loadingScreenMessage)
        }
        else -> {
            // Display a snackbar if the user has data in the cache, but encountered an error.
            // The UI will render both a snackbar error message and their cached data as opposed to
            // a fullscreen error. By placing this in StatefulScreen, this error handling approach
            // is applied to all screens in the app.
            Log.d(TAG_STATEFUL_SCREEN, "Success | UIState: $uiState")
            if (uiState.hasPartialData) {
                LaunchedEffect(key1 = uiState.error.hashCode()) {
                    uiState.error?.message?.let {
                        Log.d(TAG_STATEFUL_SCREEN, "Show snackbar error message")
                        snackbarHostState.showSnackbar(message = it)
                    }
                }
            }
            success(uiState)
        }
    }
}