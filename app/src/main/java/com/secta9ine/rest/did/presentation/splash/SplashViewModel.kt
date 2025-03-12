package com.secta9ine.rest.did.presentation.splash

import android.app.Application
import android.content.res.Resources
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val application: Application,
    private val dataStoreRepository: DataStoreRepository,
    private val restApiRepository: RestApiRepository,
    private val resources: Resources
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    var hasCheckedAutoLogin by mutableStateOf(false)
        private set
    var androidId by mutableStateOf("")
        private set
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)
        androidId = Settings.Secure.getString(application.contentResolver,
            Settings.Secure.ANDROID_ID)

        viewModelScope.launch {
            restApiRepository.getDevice(androidId).let {
                Log.d(TAG,"device info:${it}")
            }
            /*setDevice
            restApiRepository.setDevice(
                cmpCd =  "SLKR",
                salesOrgCd = "8000",
                storCd = "5000511",
                cornerCd = "CIHA",
                deviceId = androidId,
                deviceNo = "80"
            ).let {
                Log.d(TAG,"device set info:${it}")
            }
             */

//            userId = dataStoreRepository.getUserId().first()
//            if (userId.isNotEmpty()) {
//                currentFocus = "password"
//            }
//            isAutoLoginChecked = dataStoreRepository.getIsAutoLoginChecked().first()
        }

        //장비 매핑이 완료된 지 확인하고(cmp,sales,stor,coner,deviceNo,displayMenuCd,rollingYn,apiKey)
        //restApiRepository.getDevice()
        // or null check -> pass 시 장비 매핑 정보 db 업데이트 -> displayMenu로 이동
    }

    suspend fun getDevice() {

        restApiRepository.getDevice(androidId).let {
                Log.d(TAG,"device info:${it}")
        }
    }

    sealed interface UiState {
        object Loading : UiState
        object Login : UiState
        object OrderStatus : UiState
        object Product : UiState
        object Idle : UiState
        object Restart : UiState
        data class Error(val message: String) : UiState
    }
}
