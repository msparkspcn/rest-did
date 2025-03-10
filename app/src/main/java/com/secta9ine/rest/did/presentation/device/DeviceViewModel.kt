package com.secta9ine.rest.did.presentation.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.Cmp
import com.secta9ine.rest.did.domain.model.Corner
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
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
    private val restApiRepository: RestApiRepository,   //테스트
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

//    var salesOrgList: List<String> = emp
    var cmpList by mutableStateOf(emptyList<Cmp>())
    var cmpNmList by mutableStateOf(emptyList<Pair<String, String>>())
        private set
    var salesOrgNmList by mutableStateOf(emptyList<String>())
        private set
    var storNmList by mutableStateOf(emptyList<String>())
        private set
    var cornerList by mutableStateOf(emptyList<Corner>())
        private set
    var cornerNmList by mutableStateOf(emptyList<Pair<String, String>>())
        private set
    var deviceNoList by mutableStateOf(emptyList<String>())
        private set
    var displayMenuList by mutableStateOf(emptyList<String>())
        private set
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)

        viewModelScope.launch {
            _uiState.emit(UiState.Loading)

            userId = dataStoreRepository.getUserId().first()
            restApiRepository.getCmp(
               dataStoreRepository.getCmpCd().first()
            ).let { it ->
                when(it) {
                    is Resource.Success -> {
                        cmpList = it.data!!
                        Log.d(TAG,"cmpList:$cmpList")
                        cmpNmList = cmpList.map { Pair(it.cmpCd, it.cmpNm) }

                        restApiRepository.getCornerList(cmpList[0].cmpCd,"8000","")
                            .let { it ->
                                when(it) {
                                    is Resource.Success -> {
                                        cornerList = it.data!!
                                        Log.d(TAG,"cornerList:$cornerList")
                                        cornerNmList = cornerList.map { Pair(it.cornerCd, it.cornerNm)}

                                        _uiState.emit(UiState.Idle)
                                    }
                                    else -> {

                                    }
                                }
                            }
                    }
                    else -> {
//                        _uiState.emit(UiState.Error("로그인 창으로 이동합니다."))
                        Log.d(TAG,"실패 ${it.message!!}")
                        _uiState.emit(UiState.Error(it.message!!))
                    }
                }
            }
//            AuthInterceptor.initAuthToken("new_token")

            userRoleType = dataStoreRepository.getUserRoleType().first()
            when (userRoleType) {
                "001" -> {
                    Log.d(TAG,"### 관리자 계정입니다.")
                }
                "002" -> {
                    Log.d(TAG,"### 휴게소 관리자 계정입니다.")
                }
                "003" -> {
                    Log.d(TAG,"### 점포 관리자 계정입니다.")
                }
            }
            Log.d(TAG, "### 최종 userId=$userId")
            salesOrgNmList = listOf(
                "용인휴게소",
                "가평휴게소"
            )
            storNmList = listOf(
                "식당가",
                "던킨"
            )
            deviceNoList = listOf(
                "01",
                "02",
                "03",
                "04",
            )
            displayMenuList = listOf(
                "순번안내",
                "단일 메뉴",
                "메뉴 2분할",
                "메뉴 리스트",
                "스페셜 메뉴",
            )

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

    fun onSelectItem(listNm: String, item: String) {
        when(listNm) {

        }
    }

    fun getCornerList(cmpCd: String, salesOrgCd: String, storCd: String) {
        Log.d(TAG,"코너 정보 가져오기 cmpCd:$cmpCd")
        viewModelScope.launch {
            restApiRepository.getCornerList(cmpCd,salesOrgCd,storCd)
                .let { it ->
                    when(it) {
                        is Resource.Success -> {
                            cornerList = it.data!!
                            Log.d(TAG,"cornerList:$cornerList")
                            cornerNmList = cornerList.map { Pair(it.cornerCd, it.cornerNm)}

                            _uiState.emit(UiState.Idle)
                        }
                        else -> {

                        }
                    }
                }
        }
    }

    fun getDeviceList(cornerCd: String) {
        Log.d(TAG,"장비 정보 가져오기 cornerCd:$cornerCd")
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
