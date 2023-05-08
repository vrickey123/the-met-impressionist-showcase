package com.vrickey123.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.vrickey123.state.UIState
import com.vrickey123.viewmodel.ScreenViewModel

@Composable
fun <T : UIState> StatefulScreen(
    modifier: Modifier = Modifier,
    screenViewModel: ScreenViewModel<T>,
    loadingScreenTitle: String? = null,
    loadingScreenMessage: String? = null,
    success: @Composable (uiState: T) -> Unit
) {
    val uiState by screenViewModel.state.collectAsState()
    when {
        uiState.error != null -> {
            ErrorScreen(modifier, uiState.error)
        }
        uiState.loading -> {
            LoadingScreen(modifier, loadingScreenTitle, loadingScreenMessage)
        }
        else -> {
            success(uiState)
        }
    }
}