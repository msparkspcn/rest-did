package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProductList2(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val displayedProducts = state.displayedProducts
    val fullProductList = displayedProducts + List(12 - displayedProducts.size) { null }

    Column {
        Header2()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fullProductList.chunked(6).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenWidth * 0.01f)
                            .weight(1f),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        rowItems.forEach { item ->
                            if (item != null) {
                                Item2(
                                    item = item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = screenWidth * 0.005f),
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = screenWidth * 0.01f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header2() {
    Row(
        modifier = Modifier
            .background(Color(0xFF283237))
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SR DID",
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
    }
}