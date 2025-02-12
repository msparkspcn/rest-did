package com.secta9ine.rest.did.presentation.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.presentation.navigation.Screen

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
        verticalArrangement = Arrangement.SpaceBetween // 세로로 공간을 균등하게 배치
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "장비 선택",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)// 가로 가운데 정렬
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.store_nm),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.h5,
                )
                Text(
                    text = "O2POS TEST",
                    style = MaterialTheme.typography.h5
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.device_no),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.h5
                )
//                AppDropdown()
                Text(
                    text = "02",
                    style = MaterialTheme.typography.h5
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.show_menu),
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = "단일 상품",
                    style = MaterialTheme.typography.h5
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            // 롤링 옵션 (라디오 버튼)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "롤링 : ",
                    modifier = Modifier.width(150.dp),
                    style = MaterialTheme.typography.h5)
                RadioButton(
                    selected = viewModel.selectedOption == "fixed", // 기본 선택 상태
                    onClick = { viewModel.onSelectOption("fixed") }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.rolling_fix),
                    style = MaterialTheme.typography.h5)
                Spacer(modifier = Modifier.width(20.dp))
                RadioButton(
                    selected = viewModel.selectedOption == "rolling", // 기본 선택 상태
                    onClick = { viewModel.onSelectOption("rolling")}
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.rolling_rolling),
                    style = MaterialTheme.typography.h5)
            }
        }

        // 버튼들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
                    text = "확인",
                    style = MaterialTheme.typography.h5)
            }
        }
    }
}