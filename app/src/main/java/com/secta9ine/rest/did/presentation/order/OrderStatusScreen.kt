package com.secta9ine.rest.did.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun OrderStatusScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: OrderStatusViewModel = hiltViewModel()
) {
    Scaffold {
        Box(

        ) {
            Column(

            ) {
                OrderHeader()
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
            style = MaterialTheme.typography.h5,
        )
        Text(
            text = "번호가 표시되면 음식을 찾아가세요.",
            style = MaterialTheme.typography.h5,
            color = Color.White,
        )
    }
}

@Composable
fun OrderContents(
    completedOrderList: List<String> = emptyList(),
    waitingOrderList: List<String> = emptyList(),
) {
    var displayedCompletedOrders by remember { mutableStateOf(completedOrderList.take(6)) } // 처음에 6개 아이템만 표시
    var displayedWaitingOrders by remember { mutableStateOf(waitingOrderList.take(6)) } // 처음에 6개 아이템만 표시

    // 5초 후에 다음 아이템들을 추가
    LaunchedEffect(displayedCompletedOrders) {
        var currentIndex = 0
        if(completedOrderList.size<6) {
            displayedCompletedOrders = completedOrderList
        } else {
            while (true) {
                delay(3000) // 5초 대기
                val nextOrders = completedOrderList.drop(currentIndex).take(6)
                // 다음 6개를 표시
                displayedCompletedOrders = nextOrders

                currentIndex += 6

                // 마지막 항목이 남았을 때 처리
                if (currentIndex >= completedOrderList.size) {
                    currentIndex = 0 // 처음부터 다시 시작
                }

            }
        }
    }
    LaunchedEffect(displayedWaitingOrders) {
        var currentIndex2 = 0
        if (waitingOrderList.size < 6) {
            displayedWaitingOrders = waitingOrderList
        } else {
            while (true) {
                delay(3000) // 3초 대기 (시간 수정)
                displayedWaitingOrders = waitingOrderList.slice(currentIndex2 until (currentIndex2 + 6)
                    .coerceAtMost(waitingOrderList.size))
                currentIndex2 += 6

                // 마지막 아이템이 남은 경우 처리
                if (currentIndex2 >= waitingOrderList.size) {
                    currentIndex2 = 0
                }
            }
        }


    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)) {
        Box(
            modifier = Modifier
                .weight(4.5f)
                .fillMaxHeight(0.9f)
                .background((Color.White))
                .padding(12.dp)
                .border(
                    width = 2.dp,
                    color = Color.Magenta
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp, 0.dp) // Box의 중앙 정렬
            ) {
                Text(
                    text = "1133",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    fontSize = 140.sp,
                    color = Color.Magenta,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    color = Color.Magenta,
                    thickness = 4.dp // 밑줄 두께
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "주문하신 음식\n나왔습니다.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(Modifier.weight(5.5f)) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)
                ) {
                    Text(
                        text = "준비완료 | Complete",
                        style = MaterialTheme.typography.h6,
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        displayedCompletedOrders.take(3).forEach { order ->
                            Text(
                                text = order,
                                style = MaterialTheme.typography.h3,
                                modifier = Modifier.weight(1f).padding(4.dp) // 각 텍스트를 가로로 균등하게 배치
                            )
                        }
                    }

                    // 두 번째 행
                    Row(modifier = Modifier.fillMaxWidth()) {
                        displayedCompletedOrders.drop(3).take(3).forEach { order ->
                            Text(
                                text = order,
                                style = MaterialTheme.typography.h3,
                                modifier = Modifier.weight(1f).padding(4.dp) // 각 텍스트를 가로로 균등하게 배치
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)
                ) {
                    Text(
                        text = "준비중 | Preparing",
                        style = MaterialTheme.typography.h6,
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        displayedWaitingOrders.take(3).forEach { order ->
                            Text(
                                text = order,
                                style = MaterialTheme.typography.h3,
                                modifier = Modifier.weight(1f).padding(4.dp) // 각 텍스트를 가로로 균등하게 배치
                            )
                        }
                    }

                    // 두 번째 행
                    Row(modifier = Modifier.fillMaxWidth()) {
                        displayedWaitingOrders.drop(3).take(3).forEach { order ->
                            Text(
                                text = order,
                                style = MaterialTheme.typography.h3,
                                modifier = Modifier.weight(1f).padding(4.dp) // 각 텍스트를 가로로 균등하게 배치
                            )
                        }
                    }
                }
            }
        }
    }

}