package com.secta9ine.rest.did.presentation.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(

) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    var completedOrderList: List<String> =
        listOf(
            "1133", "1134", "1135",
            "1136", "1137", "1138",
            "1139", "1140", "1141",
            "1142", "1145", "1146",
            "1147", "1148"
        )
        private set

    var waitingOrderList: List<String> =
        listOf(
            "2134",
            "2135", "2136",
            "2137", "2138", "2139",
            "2140", "2141", "2142",
            "2145", "2146", "2147",
            "2148", "2149", "2150"
        )

    var callOrderNo: String?
    private set

    init {
        callOrderNo =""
    }

    fun onCallOrder(orderNo : String) {
        callOrderNo = orderNo
    }

    fun onEnterKeyPressed() {
        Log.d(TAG,"환경 설정 화면 이동")
        viewModelScope.launch {
            _uiState.emit(UiState.Device)
        }
    }

    sealed interface UiState {
        object Loading : UiState
        object Device : UiState
        object Idle : UiState
        data class Error(val message: String) : UiState

    }
}