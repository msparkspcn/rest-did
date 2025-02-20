package com.secta9ine.rest.did.presentation.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: DeviceViewModel = hiltViewModel()
) {
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
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "장비 선택",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F6F8)).padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.store_setting),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF6F777D),
                )
            }

            val itemModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp)


            Row(
                modifier = itemModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.sales_org_nm),
                    modifier = Modifier.width(150.dp),
                    style = TextStyle(fontSize = 22.sp)
                )
                Text(
                    text = "용인휴게소",
                    style = TextStyle(fontSize = 22.sp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color(0xFFCFD4D8),
                thickness = 1.dp // 밑줄 두께
            )
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = itemModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.store_nm),
                    modifier = Modifier.width(150.dp),
                    style = TextStyle(fontSize = 22.sp),
                )
                Text(
                    text = "식당가",
                    style = TextStyle(fontSize = 22.sp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color(0xFFCFD4D8),
                thickness = 1.dp // 밑줄 두께
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = itemModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.corner_nm),
                    modifier = Modifier.width(150.dp),
                    style = TextStyle(fontSize = 22.sp)
                )
                Text(
                    text = "라면",
                    style = TextStyle(fontSize = 22.sp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F6F8)).padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.device_setting),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF6F777D),
                )
            }
            Row(
                modifier = itemModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.device_no),
                    modifier = Modifier.width(150.dp),
                    style = TextStyle(fontSize = 22.sp)
                )
//                AppDropdown()
                Text(
                    text = "02",
                    style = TextStyle(fontSize = 22.sp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color(0xFFCFD4D8),
                thickness = 1.dp // 밑줄 두께
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = itemModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.show_menu),
                    modifier = Modifier.width(150.dp),
                    style = TextStyle(fontSize = 22.sp)
                )
                Text(
                    text = "단일 상품",
                    style = TextStyle(fontSize = 22.sp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color(0xFFCFD4D8),
                thickness = 1.dp // 밑줄 두께
            )
            Spacer(modifier = Modifier.height(15.dp))
            // 롤링 옵션 (라디오 버튼)
            Row(
                modifier = itemModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "롤링 : ",
                    modifier = Modifier.width(150.dp),
                    style = TextStyle(fontSize = 22.sp)
                )
                RadioButton(
                    selected = viewModel.selectedOption == "fixed", // 기본 선택 상태
                    onClick = { viewModel.onSelectOption("fixed") }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.rolling_fix),
                    style = TextStyle(fontSize = 22.sp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                RadioButton(
                    selected = viewModel.selectedOption == "rolling", // 기본 선택 상태
                    onClick = { viewModel.onSelectOption("rolling")}
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.rolling_rolling),
                    style = TextStyle(fontSize = 22.sp)
                )
            }
        }

        // 버튼들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F6F8))
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center // 가로 가운데 정렬
        ) {
            AppButton(
                modifier = Modifier.width(150.dp),
                onClick = viewModel::onLogout
            ) {
                Text(
                    text = stringResource(R.string.logout_button),
                    style = MaterialTheme.typography.h5
                )
            }

            Spacer(modifier = Modifier.width(50.dp))

            AppButton(
                modifier = Modifier.width(150.dp),
                onClick = viewModel::onShowMenu
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.h5)
            }
        }
    }
}

@Composable
fun DividerLine() {
    Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
    Spacer(modifier = Modifier.height(8.dp))
}