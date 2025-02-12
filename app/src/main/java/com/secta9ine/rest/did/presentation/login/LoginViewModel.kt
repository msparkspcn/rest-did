package com.secta9ine.rest.did.presentation.login

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val restApiRepository: RestApiRepository,
    private val resources: Resources
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

    fun onChangeText(field: String, value: String) {
        Log.d(TAG,"field:$field")
        when(field) {
            "storeCd" -> storeCd = value
            "storePassword" -> storePassword = value
        }
    }

    fun onLogin() {
        Log.d(TAG,"### 로그인 클릭 storeCd:$storeCd, password:$storePassword")
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            if(storeCd.isEmpty()) {
                _uiState.emit(UiState.Error(resources.getString(R.string.store_cd_empty_error)))
            }
            else if(storePassword.isEmpty()) {
                _uiState.emit(UiState.Error(resources.getString(R.string.store_password_empty_error)))
            }
            else {
                restApiRepository.getStoreInfo(
                    storeCd = storeCd,
                    storePassword = storePassword
                ).let {
                    when (it) {
                        is Resource.Success -> {
                            if(it.data!=null) {
                                Log.d(TAG,"data:${it.data}")
                                val store = it.data!!
                                dataStoreRepository.setStoreCd(store.storeCd)
                                _uiState.emit(UiState.Login)
                            }
                            else {

                            }
                            _uiState.emit(UiState.Idle)


                        }
                        is Resource.Failure -> {
                            _uiState.emit(UiState.Error(it.message!!))
                        }
                    }
                }
            }

        }

    }
    sealed interface UiState {
        object Loading : UiState
        object Login : UiState
        object Idle : UiState
        data class Error(val message: String) : UiState

    }
}
