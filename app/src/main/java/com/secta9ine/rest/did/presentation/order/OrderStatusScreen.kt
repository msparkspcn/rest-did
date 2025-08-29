package com.secta9ine.rest.did.presentation.order

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.navigation.NavUtils.navigateAsSecondScreen
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppAlertDialog
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.ui.component.AppLoadingIndicator
import com.secta9ine.rest.did.util.UiString
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

private const val TAG = "OrderStatusScreen"
@Composable
fun OrderStatusScreen(
    navController: NavHostController? = null,
    viewModel: OrderStatusViewModel = hiltViewModel(),
    wsViewModel: WebSocketViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    var dialogMessage by remember { mutableStateOf<UiString?>(null) }

    val uiState by viewModel.uiState.collectAsState(OrderStatusViewModel.UiState.Loading)
    val uiState2 by wsViewModel.uiState.collectAsState(initial = null)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Log.d(TAG,"screenWidth:$screenWidth")
    val density = LocalDensity.current
    val titleNmSize = with(density) { (screenWidth * 0.03f).toSp() }
    val titleMsgSize = with(density) { (screenWidth * 0.02f).toSp() }

    val displayedCompletedOrders by viewModel.displayedCompletedOrders.collectAsState()
    val displayedWaitingOrders by viewModel.displayedWaitingOrders.collectAsState()
    val currentCalledOrder by viewModel.currentCalledOrder.collectAsState()

    val socketHandler = remember(viewModel) {
        { state: WebSocketViewModel.UiState ->
            viewModel.handleSocketEvent(state)
        }
    }

    DisposableEffect(Unit) {
        wsViewModel.registerHandler(socketHandler)
        onDispose {
            wsViewModel.unregisterHandler(socketHandler)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.uiState.collect {
            when(it) {
                is OrderStatusViewModel.UiState.UpdateDevice -> {
                    Log.d(TAG,"H2222")
//                    var displayCd = viewModel.getDisplayCd()
//                    if(displayCd=="1234") {
//                        //같은 화면으로 이동할 필요 없고 업데이트된 로컬 db 조회 후 데이터 렌더링 다시 실행
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
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp) {
                        viewModel.onEnterKeyPressed()
                        Toast
                            .makeText(context, "Enter key pressed!", Toast.LENGTH_SHORT)
                            .show()
                        exitProcess(0)
                    } else {
                        false
                    }
                }
        ) {
            if(uiState is OrderStatusViewModel.UiState.Loading) {
                AppLoadingIndicator()
            }
            else {
                Column {
                    OrderHeader(
                        titleNmSize = titleNmSize,
                        titleMsgSize = titleMsgSize
                    )
                    OrderContents(
                        displayedCompletedOrders = displayedCompletedOrders,
                        displayedWaitingOrders = displayedWaitingOrders,
                        currentCalledOrder = currentCalledOrder
                    )
                }
                if (dialogMessage != null) {
                    AppAlertDialog {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight(0.6f)
                                .background(Color.White),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.alert_title),
                                        style = TextStyle(fontSize = 30.sp)
                                    )
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Text(
                                        text = dialogMessage?.asString() ?: "",
                                        style = TextStyle(fontSize = 25.sp)
                                    )
                                }

                                // 버튼을 하단에 배치
                                AppButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { dialogMessage = null },
                                    shape = RoundedCornerShape(0.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.confirm),
                                        style = TextStyle(fontSize = 28.sp)
                                    )
                                }
                            }
                        }
                    }
                }
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
            .background(Color(0xFF1EB5EC))
            .fillMaxWidth()
            .padding(20.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SR DID",
            fontSize = titleNmSize,
            color = Color.White,
        )
        Text(
            text = stringResource(id = R.string.order_info_msg),
            fontSize = titleMsgSize,
            color = Color.White,
        )
    }
}

@Composable
fun OrderContents(
    displayedCompletedOrders: List<OrderStatus?> = emptyList(),
    displayedWaitingOrders: List<OrderStatus?> = emptyList(),
    currentCalledOrder: OrderStatus? = null,
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(currentCalledOrder?.orderNoC) {
        if (currentCalledOrder != null) {
            repeat(3) {
                isVisible = true
                delay(500)
                isVisible = false
                delay(500)
            }
            isVisible = true // 마지막에는 항상 켜진 상태로 종료
        }
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
        Card(
            backgroundColor = Color.White,
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .weight(4.5f)
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                Log.d(TAG,"screenWidth:$screenWidth")
                val density = LocalDensity.current
                val textSize = with(density) { (screenWidth * 0.125f).toSp() }
                val msgTextSize = with(density) { (screenWidth * 0.05f).toSp() }

                Text(
                    text = currentCalledOrder?.orderNoC ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .alpha(if (isVisible) 1f else 0f),
                    fontSize = textSize,
                    color = Color(0xFF1EB5EC),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    color = Color(0xFF1EB5EC),
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
        Column(
            modifier = Modifier
                .weight(5.5f)
                .fillMaxHeight(0.9f)
        ) {
            val configuration2 = LocalConfiguration.current
            val screenWidth2 = configuration2.screenWidthDp.dp
            Log.d(TAG,"screenWidth2:$screenWidth2")
            val density2 = LocalDensity.current
            val statusSize = with(density2) { (screenWidth2 * 0.015f).toSp() }
            val msgTextSize = with(density2) { (screenWidth2 * 0.04f).toSp() }
            val iconSizeDp = with(LocalDensity.current) { statusSize.toDp() }
            OrderStatusCard(
                icon = Icons.Outlined.CheckCircleOutline,
                iconMultiplier = 1.15f,
                title = stringResource(id = R.string.completed_order),
                orders = displayedCompletedOrders,
                rowCount = 2,
                columnCount = 3,
                msgTextSize = msgTextSize,
                statusSize = statusSize,
                iconSizeDp = iconSizeDp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            OrderStatusCard(
                icon = Icons.Outlined.HourglassEmpty,
                iconMultiplier = 1.25f,
                title = stringResource(id = R.string.preparing_order),
                orders = displayedWaitingOrders,
                rowCount = 3,
                columnCount = 3,
                msgTextSize = msgTextSize,
                statusSize = statusSize,
                iconSizeDp = iconSizeDp,
                modifier = Modifier
                    .weight(1.5f)
                    .padding(start = 10.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 100
)
@Composable
fun PreviewOrderHeader() {
    OrderHeader(
        titleNmSize = 30.sp,
        titleMsgSize = 20.sp
    )
}

@Preview(
    name = "OrderContents Preview",
    showBackground = true,
    widthDp = 1000,
    heightDp = 600
)
@Composable
fun PreviewOrderContents() {
    val dummyOrder = OrderStatus(orderNoC = "A001")
    val completedOrders = listOf(dummyOrder, dummyOrder, dummyOrder)
    val waitingOrders = List(9) { OrderStatus(orderNoC = "W00$it") }

    OrderContents(
        displayedCompletedOrders = completedOrders,
        displayedWaitingOrders = waitingOrders,
        currentCalledOrder = OrderStatus(orderNoC = "C123")
    )
}