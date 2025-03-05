package com.secta9ine.rest.did.presentation.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val dataStoreRepository:DataStoreRepository,
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    var userId by mutableStateOf("")
        private set
    var userRoleType by mutableStateOf("")
    var selectedOption by mutableStateOf("fixed")
        private set

    var deviceCdList: List<String> = emptyList()
        private set

    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)

        viewModelScope.launch {
            userId = dataStoreRepository.getUserId().first()
            userRoleType = dataStoreRepository.getUserRoleType().first()
            if(userRoleType=="001") {
                Log.d(TAG,"### 관리자 계정입니다.")
            }
            else if(userRoleType=="002") {
                Log.d(TAG,"### 휴게소 관리자 계정입니다.")
            }
            else if(userRoleType=="003") {
                Log.d(TAG,"### 점포 관리자 계정입니다.")
            }
            Log.d(TAG, "### 최종 userId=$userId")
        }
    }
    fun onLogout() {
        Log.d(TAG,"### 로그아웃 클릭")
        viewModelScope.launch {
            _uiState.emit(UiState.Logout)
        }
    }

    fun onShowMenu() {
        viewModelScope.launch {
            _uiState.emit(UiState.OrderStatus)
        }
    }

    fun onSelectOption(option: String) {
        selectedOption = option
    }

    fun onEnterKeyPressed() {
        Log.d(TAG,"장비설정화면 이동")
        viewModelScope.launch {
            _uiState.emit(UiState.OrderStatus)
        }
    }

    fun onBackSpacePressed() {
        Log.d(TAG,"로그인 화면 이동")
        viewModelScope.launch {
            _uiState.emit(UiState.Logout)
        }
    }



    sealed interface UiState {
        object Loading : UiState
        object Logout : UiState
        object OrderStatus : UiState
        object Idle : UiState
        data class Error(val message: String) : UiState

    }
}
