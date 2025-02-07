package com.secta9ine.rest.did.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.ui.component.AppTextInput
import com.secta9ine.rest.did.ui.theme.RESTDIDTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: LoginViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "SR DID"
        )
        Text(
            text = "MEMBER LOGIN"
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppTextInput(
            text = viewModel.storeCd,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF37454B))
                .clickable { viewModel.onChangeFocus("posUserId") },
            color = Color(0xFFFFFFFF),
            focussed = viewModel.currentFocus == "posUserId"
        )
        Spacer(Modifier.height(8.dp))
        AppTextInput(
            text = "⬤".repeat(viewModel.storePassword.length),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF37454B))
                .clickable { viewModel.onChangeFocus("storePassword") },
            color = Color(0xFFFFFFFF),
            focussed = viewModel.currentFocus == "storePassword"
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { /* Handle button click */ }) {
            Text("로그인")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    RESTDIDTheme {
        LoginScreen()
    }
}