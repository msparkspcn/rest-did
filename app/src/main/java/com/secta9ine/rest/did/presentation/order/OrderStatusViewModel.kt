package com.secta9ine.rest.did.presentation.order

import androidx.lifecycle.ViewModel
import com.secta9ine.rest.did.presentation.device.DeviceViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(

) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<DeviceViewModel.UiState>()
    val uiState = _uiState.asSharedFlow()

    var completedOrderList: List<String> =
        listOf(
            "1134", "1135", "1136",
            "1137", "1138", "1139",
            "1140", "1141", "1142",
            "1145", "1146", "1147",
            "1148"
        )
        private set

    var waitingOrderList: List<String> =
        listOf(
            "2134", "2135", "2136",
            "2137", "2138", "2139",
            "2140", "2141", "2142",
            "2145", "2146", "2147",
            "2148"
        )


}