package com.secta9ine.rest.did.login;

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    var currentFocus by mutableStateOf("storeCd")
        private set
    var storeCd by mutableStateOf("")
        private set
    var storePassword by mutableStateOf("")
        private set



    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)

        viewModelScope.launch {
            storeCd = dataStoreRepository.getStoreCd().first()
            if (storeCd.isNotEmpty()) {
                currentFocus = "posPassword"
            }
            Log.d(TAG, "### 최종 매장코드=$storeCd")
        }
    }

    fun onChangeFocus(value: String) {
        currentFocus = value
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Login : UiState
        data object Idle : UiState
        data class Error(val message: String) : UiState

    }
}
