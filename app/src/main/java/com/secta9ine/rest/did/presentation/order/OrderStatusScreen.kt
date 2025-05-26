package com.secta9ine.rest.did.presentation.order

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.navigation.NavUtils.navigateAsSecondScreen
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.util.UiString
import kotlinx.coroutines.delay

private const val TAG = "OrderStatusScreen"
@Composable
fun OrderStatusScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: OrderStatusViewModel = hiltViewModel(),
    wsViewModel: WebSocketViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    var dialogMessage by remember { mutableStateOf<UiString?>(null) }

    val uiState by viewModel.uiState.collectAsState(initial = null)
    val uiState2 by wsViewModel.uiState.collectAsState(initial = null)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Log.d(TAG,"screenWidth:$screenWidth")
    val density = LocalDensity.current
    val titleNmSize = with(density) { (screenWidth * 0.02f).toSp() }
    val titleMsgSize = with(density) { (screenWidth * 0.02f).toSp() }
    val msgTextSize = with(density) { (screenWidth * 0.05f).toSp() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.uiState.collect {
            when(it) {
                is OrderStatusViewModel.UiState.UpdateDevice -> {
                    Log.d(TAG,"H2222")
//                    var displayCd = viewModel.getDisplayCd()
//                    if(displayCd=="1234") {
//                        //같은 화면으로 이동할 필요 없고 업데이트된 로컬 db에서 조회에서 데이터 렌더링 다시 실행
//                    }
//                    else {
//                        navController?.navigateAsSecondScreen(Screen.ProductScreen.route)
//                    }
                    Log.d(TAG,"OrderStatusViewModel updateDevice")
                    navController?.navigateAsSecondScreen(Screen.ProductScreen.route)
                }
                is OrderStatusViewModel.UiState.Error -> {
                    dialogMessage = UiString.TextString(it.message)
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(uiState2) {
        when(uiState2) {
            is WebSocketViewModel.UiState.UpdateDevice -> {
                Log.d(TAG,"ws updateDevice")
                viewModel.updateUiState(OrderStatusViewModel.UiState.UpdateDevice)
            }
            else->{}
        }
    }
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0))
                .focusRequester(focusRequester) // 포커스 요청
                .focusable() // 키 입력을 받으려면 필수
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp) {
                        viewModel.onEnterKeyPressed()
                        Toast
                            .makeText(context, "Enter key pressed!", Toast.LENGTH_SHORT)
                            .show()
                        true
                    } else {
                        false
                    }
                }
        ) {
            Column {
                OrderHeader(
                    titleNmSize = titleNmSize,
                    titleMsgSize = titleMsgSize
                )
                OrderContents(
                    completedOrderList = viewModel.completedOrderList,
                    waitingOrderList = viewModel.waitingOrderList
                )
            }
        }
    }
}

@Composable
fun OrderHeader(
    titleNmSize:TextUnit,
    titleMsgSize:TextUnit
) {
    Row(
        modifier = Modifier
            .background(Color(0xFF283237))
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "SR DID",
            fontSize = titleNmSize,
            color = Color(0xFF1BAAFE),
        )
        Text(
            text = "번호가 표시되면 음식을 찾아가세요.",
            fontSize = titleMsgSize,
            color = Color.White,
        )
    }
}

@Composable
fun OrderContents(
    completedOrderList: List<String> = emptyList(),
    waitingOrderList: List<String> = emptyList(),
) {
    var displayedCompletedOrders by remember { mutableStateOf(completedOrderList.take(6)) }
    var displayedWaitingOrders by remember { mutableStateOf(waitingOrderList.take(9)) }
    var complitedOrderIndex by remember { mutableStateOf(0) }
    var waitingOrderIndex by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if(completedOrderList.size<9) {
            displayedCompletedOrders = completedOrderList
        } else {
            while (true) {
                delay(5000) // 5초 대기

                if(complitedOrderIndex + 6 <= completedOrderList.size) {

                    displayedCompletedOrders = completedOrderList.subList(complitedOrderIndex, complitedOrderIndex + 6)
                    complitedOrderIndex += 6
                }
                else {
                    displayedCompletedOrders =completedOrderList.subList(complitedOrderIndex, completedOrderList.size)
                    complitedOrderIndex = 0
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        if (waitingOrderList.size < 9) {
            displayedWaitingOrders = waitingOrderList
        } else {
            while (true) {
                delay(5000) // 3초 대기 (시간 수정)
                if(waitingOrderIndex + 9 <= waitingOrderList.size) {
                    displayedWaitingOrders = waitingOrderList.subList(waitingOrderIndex,waitingOrderIndex + 9)
                    waitingOrderIndex += 9
                }
                else {
                    displayedWaitingOrders = waitingOrderList.subList(waitingOrderIndex, waitingOrderList.size)
                    waitingOrderIndex = 0
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        repeat(3) { // 3번 반복
            delay(500) // 0.5초 켜짐
            isVisible = false
            delay(500) // 0.5초 꺼짐
            isVisible = true
        }
    }



    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)) {
        Box(
            modifier = Modifier
                .weight(4.5f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(8.dp))
                .background((Color.White))
                .border(
                    width = 2.dp,
                    color = Color(0xFF1BAAFE),
                    RoundedCornerShape(8.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(20.dp, 0.dp)
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                Log.d(TAG,"screenWidth:$screenWidth")
                val density = LocalDensity.current
                val textSize = with(density) { (screenWidth * 0.15f).toSp() }
                val msgTextSize = with(density) { (screenWidth * 0.05f).toSp() }

                Text(
                    text = "1132",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .alpha(if (isVisible) 1f else 0f),
                    fontSize = textSize,
                    color = Color(0xFF1BAAFE),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    color = Color(0xFF1BAAFE),
                    thickness = 4.dp // 밑줄 두께
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = stringResource(id = R.string.completed_order_msg),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = msgTextSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(Modifier.weight(5.5f)) {
            val configuration2 = LocalConfiguration.current
            val screenWidth2 = configuration2.screenWidthDp.dp
            Log.d(TAG,"screenWidth2:$screenWidth2")
            val density2 = LocalDensity.current
            val statusSize = with(density2) { (screenWidth2 * 0.025f).toSp() }
            val msgTextSize = with(density2) { (screenWidth2 * 0.05f).toSp() }
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(20.dp, 0.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                ) {

                    Text(
                        text = "준비완료 | Complete",
                        fontSize = statusSize,
                    )
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        displayedCompletedOrders.take(3).forEach { order ->
                            Text(
                                text = order,
                                fontSize = msgTextSize,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    // 두 번째 행
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        displayedCompletedOrders.drop(3).take(3).forEach { order ->
                            Text(
                                text = order,
                                fontSize = msgTextSize,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(20.dp, 0.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "준비중 | Preparing",
                        fontSize = statusSize,
                    )
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        displayedWaitingOrders.take(3).forEach { order ->
                            Text(
                                text = order,
                                fontSize = msgTextSize,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    // 두 번째 행
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        displayedWaitingOrders.drop(3).take(3).forEach { order ->
                            Text(
                                text = order,
                                fontSize = msgTextSize,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        displayedWaitingOrders.drop(6).take(3).forEach { order ->
                            Text(
                                text = order,
                                fontSize = msgTextSize,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}