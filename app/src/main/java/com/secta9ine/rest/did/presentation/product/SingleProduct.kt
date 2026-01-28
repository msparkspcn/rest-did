package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.secta9ine.rest.did.util.formatCurrency

@Composable
fun SingleProduct(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val itemNmSize = with(density) { (screenWidth * 0.05f).toSp() }
    val itemNmEnSize = with(density) { (screenWidth * 0.02f).toSp() }
    val priceSize = with(density) { (screenWidth * 0.09f).toSp() }
    val unitSize = with(density) { (screenWidth * 0.06f).toSp() }

    val item = state.displayedProducts.getOrNull(0) ?: return

    Row {
        Column(
            modifier = Modifier
                .padding(40.dp, 60.dp, 40.dp, 40.dp)
                .weight(4f)
        ) {
            Text(
                text = item.itemNm,
                fontSize = itemNmSize,
                fontWeight = FontWeight.Bold,
            )
            item.itemNmEn?.let {
                Text(
                    text = it,
                    fontSize = itemNmEnSize,
                    color = Color(0xFF1EB5EC)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color(0xFF1EB5EC),
                thickness = 2.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = "${item.price}".formatCurrency() ?: "0",
                    fontSize = priceSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1EB5EC)
                )
                Text(
                    text = "원",
                    fontSize = unitSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444444)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight()
                .background((Color.White))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imgPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = "content",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
