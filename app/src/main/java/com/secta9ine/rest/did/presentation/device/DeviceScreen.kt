package com.secta9ine.rest.did.presentation.device

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeviceScreen(
    navController: NavHostController? = null,
    viewModel: DeviceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.uiState.collect {
            when(it) {
                is DeviceViewModel.UiState.Logout -> {
                    navController?.navigate(Screen.LoginScreen.route)
                }
                is DeviceViewModel.UiState.OrderStatus -> {
                    navController?.navigate(Screen.OrderStatusScreen.route)
                }

                else -> {}
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester) // 포커스 요청
            .focusable() // 키 입력을 받으려면 필수
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                    viewModel.onBackSpacePressed() // ViewModel에 이벤트 전달
                    Toast.makeText(context, "뒤로가기 key pressed!", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    Toast.makeText(context, "다른키 key pressed!", Toast.LENGTH_SHORT).show()
                    false
                }
            }
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "장비 선택",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F6F8))
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.store_setting),
                modifier = Modifier.fillMaxWidth(0.2f).padding(25.dp, 10.dp),
                style = TextStyle(fontSize = 17.sp),
                color = Color(0xFF6F777D),
            )
        }

        DeviceInfo(
            infoNm = stringResource(id = R.string.sales_org_nm),
            infoValue = "용인 휴게소",
            dividerUse = true
        )

        DeviceInfo(
            infoNm = stringResource(id = R.string.store_nm),
            infoValue = "식당가",
            dividerUse = true
        )
        DeviceInfo(
            infoNm = stringResource(id = R.string.corner_nm),
            infoValue = "라면",
            dividerUse = false
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F6F8))
                .padding(vertical = 5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.device_setting),
                modifier = Modifier.fillMaxWidth(0.2f).padding(25.dp, 10.dp),
                style = TextStyle(fontSize = 17.sp),
                color = Color(0xFF6F777D),
            )
        }
        DeviceInfo(
            infoNm = stringResource(id = R.string.device_no),
            infoValue = "02",
            dividerUse = true
        )
        DeviceInfo(
            infoNm = stringResource(id = R.string.show_menu),
            infoValue = "단일 상품",
            dividerUse = true
        )
//        Spacer(modifier = Modifier.height(10.dp))
        // 롤링 옵션 (라디오 버튼)
        Row(
            modifier = Modifier.fillMaxWidth().padding(25.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "롤링 : ",
                modifier = Modifier.fillMaxWidth(0.2f),
                style = TextStyle(fontSize = 19.sp)
            )
            RadioButton(
                selected = viewModel.selectedOption == "fixed", // 기본 선택 상태
                onClick = { viewModel.onSelectOption("fixed") }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.rolling_fix),
                style = TextStyle(fontSize = 19.sp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            RadioButton(
                selected = viewModel.selectedOption == "rolling", // 기본 선택 상태
                onClick = { viewModel.onSelectOption("rolling")}
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.rolling_rolling),
                style = TextStyle(fontSize = 19.sp)
            )
        }
        // 버튼들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F6F8))
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center // 가로 가운데 정렬
        ) {
            AppButton(
                modifier = Modifier.width(150.dp),
                onClick = viewModel::onLogout
            ) {
                Text(
                    text = stringResource(R.string.logout_button),
                    style = MaterialTheme.typography.h6
                )
            }

            Spacer(modifier = Modifier.width(50.dp))

            AppButton(
                modifier = Modifier.width(150.dp),
                onClick = viewModel::onShowMenu
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable
fun DeviceInfo(
    infoNm: String,
    infoValue: String,
    dividerUse : Boolean
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(25.dp, 10.dp),
//                .background(Color.Yellow),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.2f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = infoNm,
                    style = TextStyle(fontSize = 19.sp)
                )
            }
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = infoValue,
                    style = TextStyle(fontSize = 19.sp)
                )
            }
        }
        if(dividerUse) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp, 5.dp),
                color = Color(0xFFCFD4D8),
                thickness = 1.dp // 밑줄 두께
            )
        }
    }
}