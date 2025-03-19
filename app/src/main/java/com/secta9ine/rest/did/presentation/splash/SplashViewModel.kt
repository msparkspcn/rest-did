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
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.domain.usecase.RegisterUseCases
import com.secta9ine.rest.did.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val application: Application,
    private val restApiRepository: RestApiRepository,
    private val deviceRepository: DeviceRepository,
    private val registerUseCases: RegisterUseCases,
    private val dataStoreRepository: DataStoreRepository,
    private val resources: Resources
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    var androidId by mutableStateOf("")
        private set
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)
        androidId = Settings.Secure.getString(application.contentResolver,
            Settings.Secure.ANDROID_ID)

        viewModelScope.launch {
            dataStoreRepository.setDeviceId(androidId)
            restApiRepository.checkDevice(androidId).let {
                when(it) {
                    is Resource.Success -> {
                        if(it.data!=null) {  //인증된 경우 재등록 필요 없음.
                            var device = it.data
                            if(device.apiKey!=null) {
                                RestApiService.updateAuthToken(device.apiKey!!)
                            }
                            /*상품, 주문 마스터 수신 전까지 주석
                            registerUseCases.fetch(device).let {
                                when(it) {
                                    is Resource.Success -> {
                                        Log.d(TAG,"마스터 수신 성공")
                                        if(it.data!=null) {
                                            registerUseCases.register(it.data!!)
                                            _uiState.emit(UiState.UpdateDevice)
                                        } else {

                                        }

                                    }
                                    is Resource.Failure -> {
                                        Log.d(TAG,"설정완료 필요 it:$it")
                                    }
                                }
                            }
                            */
                            if(it.data!=null) {
                                registerUseCases.register(device)
                                _uiState.emit(UiState.UpdateDevice)
                            } else {

                            }
                        }
                        else {
                            restApiRepository.registerDeviceId(androidId).let {    //등록
                                Log.d(TAG,"device info:${it}")
                            }
                        }
                    }
                    is Resource.Failure -> {}
                }

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

    suspend fun updateUiState(state: UiState) {
        _uiState.emit(state)
    }

    suspend fun getDisplayMenuCd(): String {
        val device = deviceRepository.getDevice(
            androidId
        ).firstOrNull() ?: throw RuntimeException("")
        Log.d(TAG,"device:$device")
        return device.displayMenuCd!!

    }

    sealed interface UiState {
        object Loading : UiState
        object Login : UiState
        object OrderStatus : UiState
        object Product : UiState
        object Idle : UiState
        object Restart : UiState
        object UpdateDevice : UiState
        data class Error(val message: String) : UiState
    }
}
