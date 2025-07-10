package com.secta9ine.rest.did.presentation.splash

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    private val restApiRepository: RestApiRepository,
    private val deviceRepository: DeviceRepository,
    private val registerUseCases: RegisterUseCases,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    private val _androidId = MutableStateFlow("")


    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> get() = _permissionGranted

    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)
        _androidId.value = Settings.Secure.getString(application.contentResolver,
            Settings.Secure.ANDROID_ID)

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


        //장비 매핑이 완료된 지 확인하고(cmp,sales,stor,corner,deviceNo,displayMenuCd,rollingYn,apiKey)
        //restApiRepository.getDevice()
        // or null check -> pass 시 장비 매핑 정보 db 업데이트 -> displayMenu 로 이동
    }
    fun onPermissionResult(isGranted: Boolean) {
        _permissionGranted.value = isGranted
        if(isGranted) {
            viewModelScope.launch {
                Log.d(TAG,"here")
                dataStoreRepository.setDeviceId(_androidId.value)
                checkDevice()
            }
        }
    }

    suspend fun checkDevice() {
        _uiState.emit(UiState.Loading)
        Log.d(TAG,"checkDevice androidId:${_androidId.value}")
        when (val result = restApiRepository.checkDevice(_androidId.value)) {
            is Resource.Success -> {
                val device = result.data
                if (device != null) {
                    Log.d(TAG,"apiKey:${device.apiKey}")
                    device.apiKey?.let { RestApiService.updateAuthToken(it) }
                    dataStoreRepository.setCmpCd(device.cmpCd!!)
                    dataStoreRepository.setSalesOrgCd(device.salesOrgCd!!)
                    dataStoreRepository.setStorCd(device.storCd!!)
                    dataStoreRepository.setCornerCd(device.cornerCd!!)
                    // 상품, 주문 마스터 수신 전까지 주석
                    /**/
                    when (val masterResult = registerUseCases.fetch(device)) {
                        is Resource.Success -> {
                            Log.d(TAG, "마스터 수신 성공")
                            masterResult.data?.let {
                                registerUseCases.register(it)
                                _uiState.emit(UiState.UpdateDevice)
                            }
                        }
                        is Resource.Failure -> {
                            Log.d(TAG, "마스터 수신 실패 it:$masterResult")
                            _uiState.emit(UiState.Idle)
                        }
                    }
                }
                else {
                    Log.d(TAG,"result:${result.message}")
                    _uiState.emit(UiState.Idle)
                }
            }
            is Resource.Failure -> {
                if(result.message!=null) {
                    if(result.message.contains("등록되지 않은")) {
                        Log.d(TAG,"등록되지 않은 장비")
                        _uiState.emit(UiState.Idle)
                        registerNewDevice()
                    }
                    else if(result.message.contains("인증되지 않은")) {
                        Log.d(TAG,"인증되지 않은 장비")
                        _uiState.emit(UiState.Error(result.message!!))
                    }
                }
                Log.d(TAG, "Device check 실패: ${result.data}, ${result.message}")
            }
        }
    }

    private suspend fun registerNewDevice() {
        val registerResult = restApiRepository.registerDeviceId(_androidId.value)
        Log.d(TAG, "device info: $registerResult")
    }
    suspend fun updateUiState(state: UiState) {
        _uiState.emit(state)
    }

    suspend fun getDisplayMenuCd(): String {
        val device = deviceRepository.getDevice(
            _androidId.value
        ).firstOrNull() ?: throw RuntimeException("")
        Log.d(TAG,"device:$device")
        return device.displayMenuCd!!

    }
    suspend fun checkPermissions(context: Context) {
        // Check if permissions are granted
        Log.d(TAG,"권한 확인")
        val writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        _permissionGranted.value = (writePermission == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED)
        Log.d(TAG,"권한 확인 ${_permissionGranted.value}")
        if(_permissionGranted.value==false) {
            _uiState.emit(UiState.GetPermission)
        }
    }


    sealed interface UiState {
        object Loading : UiState
        object Login : UiState
        object OrderStatus : UiState
        object Product : UiState
        object Idle : UiState
        object Restart : UiState
        object UpdateDevice : UiState
        object CheckDevice : UiState
        object GetPermission : UiState
        data class Error(val message: String) : UiState
    }
}
