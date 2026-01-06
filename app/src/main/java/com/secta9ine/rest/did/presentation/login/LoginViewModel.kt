package com.secta9ine.rest.did.presentation.login

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.remote.api.RestApiService
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
import kotlin.system.exitProcess

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val restApiRepository: RestApiRepository,
    private val resources: Resources
) : ViewModel() {
    private val tag = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    var currentFocus by mutableStateOf("userId")
        private set
    var userId by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var isAutoLoginChecked: String = "N"
        private set

    var hasCheckedAutoLogin by mutableStateOf(false)
        private set

    init {
        uiState.onEach { Log.d(tag, "uiState=$it") }.launchIn(viewModelScope)

        viewModelScope.launch {
            userId = dataStoreRepository.getUserId().first()
            if (userId.isNotEmpty()) {
                currentFocus = "password"
            }
            isAutoLoginChecked = dataStoreRepository.getIsAutoLoginChecked().first()
        }
    }

//    fun checkAutoLogin() {
//        Log.d(TAG, "1.hasCheckedAutoLogin:$hasCheckedAutoLogin")
//        if(hasCheckedAutoLogin) return
//
//        viewModelScope.launch {
//            isAutoLoginChecked = dataStoreRepository.getIsAutoLoginChecked().first()
//            if (isAutoLoginChecked == "Y") {
//                Log.d(TAG, "자동 로그인 시작")
//                hasCheckedAutoLogin = true
//                Log.d(TAG, "2.hasCheckedAutoLogin:$hasCheckedAutoLogin")
//                _uiState.emit(UiState.Login)
//            }
//        }
//    }
    fun checkAutoLogin() {
        viewModelScope.launch {
            isAutoLoginChecked = dataStoreRepository.getIsAutoLoginChecked().first()
            Log.d(tag,"isAutoLoginChecked:$isAutoLoginChecked")
            if (isAutoLoginChecked == "Y") {
                Log.d(tag, "자동 로그인 시작")
                password = dataStoreRepository.getPassword().first()
                onLogin()
//                _uiState.emit(UiState.Login)
            } else {

            }
        }
    }

    fun onChangeFocus(value: String) {
        Log.d(tag,"onChangeFocus currentFocus:$value")
        currentFocus = value
    }

    fun onChangeText(field: String, value: String) {
        when(field) {
            "userId" -> userId = value
            "password" -> password = value
        }
    }

   fun onChangeAutoLoginChecked(currentState: String) {
        Log.d(tag,"1.체크 상태:$currentState")
        if(currentState=="N") {
            Log.d(tag,"2.isAutoLoginChecked Y로 변경")
            isAutoLoginChecked ="Y"
        }
        else isAutoLoginChecked = "N"
    }

    fun onLogin() {
        Log.d(tag,"### 로그인 클릭 userId:$userId, password:$password")
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            if(userId.isEmpty()) {
                _uiState.emit(UiState.Error(resources.getString(R.string.user_id_empty_error)))
            }
            else if(password.isEmpty()) {
                _uiState.emit(UiState.Error(resources.getString(R.string.password_empty_error)))
            }
            else {
                restApiRepository.acceptLogin(
                    userId = userId,
                    password = password
                ).let {
                    when (it) {
                        is Resource.Success -> {
                            if(it.data!=null) {
                                Log.d(tag,"data:${it.data}")
                                val user = it.data!!
                                dataStoreRepository.setUserId(user.userId)
                                dataStoreRepository.setPassword(password)
                                dataStoreRepository.setCmpCd(user.cmpCd)
                                RestApiService.updateAuthToken(user.apiKey)
                                Log.d(tag,"3.isAutoLoginChecked:$isAutoLoginChecked")
                                dataStoreRepository.setIsAutoLoginChecked(isAutoLoginChecked)
//                                RestApiService.updateAuthToken("1234")
                                if(user.userRoleType!=null) {
                                    dataStoreRepository.setUserRoleType(user.userRoleType)
                                    when(user.userRoleType) {
                                        "001" -> {
                                            dataStoreRepository.setSalesOrgCd("")
                                            dataStoreRepository.setStorCd("")
                                        }
                                        "002" -> {
                                            user.salesOrgCd?.let { it1 -> dataStoreRepository.setSalesOrgCd(it1) }
                                            dataStoreRepository.setStorCd("")
                                        }
                                        "003" -> {
                                            user.salesOrgCd?.let { it1 -> dataStoreRepository.setSalesOrgCd(it1) }
                                            user.storCd?.let { it1 -> dataStoreRepository.setStorCd(it1) }
                                        }

                                    }
                                }
                                _uiState.emit(UiState.Login)
                            }
                            else {
                                Log.d(tag,"로그인 실패")
                            }
                        }
                        is Resource.Failure -> {
                            exitProcess(0)
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
        object Restart : UiState
        data class Error(val message: String) : UiState

    }


}
