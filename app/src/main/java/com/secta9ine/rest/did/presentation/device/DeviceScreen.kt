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
        Column {
            Text(
                text = "장비 선택",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.CenterHorizontally) // 가로 가운데 정렬
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.store_nm),
                )
                Text(
                    text = "O2POS TEST",
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.device_no),
                )
                Text(
                    text = "02",
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.show_menu),
                )
                Text(
                    text = "단일 상품",
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            // 롤링 옵션 (라디오 버튼)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "롤링 : ")
                RadioButton(
                    selected = viewModel.selectedOption == "fixed", // 기본 선택 상태
                    onClick = { viewModel.onSelectOption("fixed") }
                )
                Text(text = stringResource(R.string.rolling_fix))
                RadioButton(
                    selected = viewModel.selectedOption == "rolling", // 기본 선택 상태
                    onClick = { viewModel.onSelectOption("rolling")}
                )
                Text(text = stringResource(R.string.rolling_rolling))
            }
        }

        // 버튼들
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center // 가로 가운데 정렬
        ) {
            AppButton(onClick = { viewModel::onClickLogout }) {
                Text(text = stringResource(R.string.logout_button))
            }

            Spacer(modifier = Modifier.width(50.dp))

            AppButton(onClick = { /* TODO: 확인 로직 */ }) {
                Text(text = "확인")
            }
        }
    }
}