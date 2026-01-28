package com.secta9ine.rest.did.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.secta9ine.rest.did.domain.model.ProductVo

@Composable
fun SpecialProductList(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column {
        SpecialProductHeader()
        SpecialProductContents(
            displayedSpecialProducts = state.displayedProducts,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )
    }
}

@Composable
fun SpecialProductHeader() {
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
        Text(
            text = "  Special Menu",
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
    }
}

@Composable
fun SpecialProductContents(
    displayedSpecialProducts: List<ProductVo>,
    screenWidth: Dp,
    screenHeight: Dp,
) {
    val fullProductList = displayedSpecialProducts + List(3 - displayedSpecialProducts.size) { null }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
                .padding(2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            fullProductList.forEachIndexed { index, item ->
                key(item?.itemCd ?: index) {
                    if (item != null) {
                        SpecialItem(
                            item = item,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(
                                    horizontal = screenWidth * 0.01f,
                                    vertical = screenHeight * 0.005f
                                ),
                            isEven = (index % 2 == 0)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(
                                    horizontal = screenWidth * 0.01f,
                                    vertical = screenHeight * 0.005f
                                )
                        )
                    }
                }
            }
        }
    }
}