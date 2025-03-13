package com.secta9ine.rest.did.presentation.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.data.remote.api.AuthInterceptor
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.domain.model.Cmp
import com.secta9ine.rest.did.domain.model.Corner
import com.secta9ine.rest.did.domain.model.SalesOrg
import com.secta9ine.rest.did.domain.model.Stor
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    var cmpList by mutableStateOf(emptyList<Cmp>())
    var cmpNmList by mutableStateOf(emptyList<Pair<String, String>>())
        private set
    var salesOrgList by mutableStateOf(emptyList<SalesOrg>())
        private set
    var salesOrgNmList by mutableStateOf(emptyList<Pair<String, String>>())
        private set
    var storList by mutableStateOf(emptyList<Stor>())
    var storNmList by mutableStateOf(emptyList<Pair<String, String>>())
        private set
    var cornerList by mutableStateOf(emptyList<Corner>())
        private set
    var cornerNmList by mutableStateOf(emptyList<Pair<String, String>>())
        private set
    var deviceNoList by mutableStateOf(emptyList<String>())
        private set
    var displayMenuList by mutableStateOf(emptyList<String>())
        private set
    var cmpCd by mutableStateOf("")
    var salesOrgCd by mutableStateOf("")
    var storCd by mutableStateOf("")
    var cornerCd by mutableStateOf("")
    private var jobInit: Job
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)

        jobInit = viewModelScope.launch {
            RestApiService.updateAuthToken("561C4A288F7DB058E6491852BAC98AA1B7BC54CECA2A8C1F90")
            userId = dataStoreRepository.getUserId().first()
//            cmpCd = dataStoreRepository.getCmpCd().first()
            cmpCd = "SLKR"
//            salesOrgCd = dataStoreRepository.getSalesOrgCd().first()
            salesOrgCd = "8000"
//            storCd = dataStoreRepository.getStorCd().first()
            storCd = "5000511"
            cornerCd = dataStoreRepository.getCornerCd().first()
            getCmpList(cmpCd)

            userRoleType = dataStoreRepository.getUserRoleType().first()
            when (userRoleType) {
                "001" -> {
                    Log.d(TAG,"### 관리자 계정 입니다.")
                }
                "002" -> {
                    Log.d(TAG,"### 휴게소 관리자 계정 입니다.")
                }
                "003" -> {
                    Log.d(TAG,"### 점포 관리자 계정 입니다.") //cmp,sales,stor 다 있는 경우
                }
            }
            Log.d(TAG, "### 최종 userId=$userId")

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

    fun reload() {
        viewModelScope.launch {
            _uiState.emit(UiState.Init)
            jobInit.join()
//            _uiState.emit(UiState.Idle)
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
        Log.d(TAG,"코너 정보 cmpCd:$cmpCd, salesOrgCd:$salesOrgCd, storCd:$storCd")
        viewModelScope.launch {
//            _uiState.emit(UiState.Loading)
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

    fun getCmpList(cmpCd: String) {
        Log.d(TAG,"회원사 정보 cmpCd:$cmpCd")
        viewModelScope.launch {
//            _uiState.emit(UiState.Loading)
            restApiRepository.getCmpList(cmpCd)
                .let { it ->
                    when(it) {
                        is Resource.Success -> {
                            cmpList = it.data!!
                            Log.d(TAG,"cmpList:$cmpList")
                            cmpNmList = cmpList.map { Pair(it.cmpCd, it.cmpNm)}

                            getSalesOrgList(cmpCd)

//                            _uiState.emit(UiState.Idle)
                        }
                        else -> {

                        }
                    }
                }
        }
    }

    fun getSalesOrgList(cmpCd: String) {
        Log.d(TAG,"휴게소 정보 cmpCd:$cmpCd")
        viewModelScope.launch {
//            _uiState.emit(UiState.Loading)
            restApiRepository.geSalesOrgList(cmpCd)
                .let { it ->
                    when(it) {
                        is Resource.Success -> {
                            salesOrgList = it.data!!
                            Log.d(TAG,"salesOrgList:$salesOrgList")
                            salesOrgNmList = salesOrgList.map { Pair(it.salesOrgCd, it.salesOrgNm)}

                            if(salesOrgCd=="") {
                                getStorList(cmpCd, salesOrgList[0].salesOrgCd)
                            }
                            else {
                                getStorList(cmpCd, salesOrgCd)
                            }

//                            _uiState.emit(UiState.Idle)
                        }
                        else -> {


                        }
                    }
                }
        }
    }

    fun getStorList(cmpCd: String, salesOrgCd: String) {
        Log.d(TAG,"점포 정보 cmpCd:$cmpCd, salesOrgCd:$salesOrgCd")
        viewModelScope.launch {
//            _uiState.emit(UiState.Loading)
            restApiRepository.geStorList(cmpCd,salesOrgCd)
                .let { it ->
                    when(it) {
                        is Resource.Success -> {
                            storList = it.data!!
                            Log.d(TAG,"storList:$storList")
                            storNmList = storList.map { Pair(it.storCd, it.storNm)}
                            if(storCd=="") {
                                getCornerList(cmpCd, salesOrgCd, storList[0].storCd)
                            }
                            else {
                                getCornerList(cmpCd, salesOrgCd, storCd)
                            }
//                            _uiState.emit(UiState.Idle)
                        }
                        else -> {

                        }
                    }
                }
        }
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
        object Init : UiState
        object Loading : UiState
        object Logout : UiState
        object OrderStatus : UiState
        object Idle : UiState
        data class Error(val message: String) : UiState

    }
}
