package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.model.ProductVo
import kotlinx.coroutines.delay

private const val TAG = "SingleProduct"
@Composable
fun SingleProduct(
    productList: List<ProductVo>,
    rollingYn: String,
) {
    var displayedProducts by remember { mutableStateOf(productList.take(1)) }
    var productIndex by remember { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Log.d(TAG,"screenWidth:$screenWidth")
    val density = LocalDensity.current
    val itemNmSize = with(density) { (screenWidth * 0.05f).toSp() }
    val itemNmEnSize = with(density) { (screenWidth * 0.02f).toSp() }
    val priceSize = with(density) { (screenWidth * 0.1f).toSp() }
    val unitSize = with(density) { (screenWidth * 0.07f).toSp() }
    LaunchedEffect(productList) {
        Log.d(TAG,"productList 변경")
        // 새로운 productList가 들어올 때마다 초기화
        productIndex = 0
        displayedProducts = productList.take(1)
    }

    LaunchedEffect(productList, rollingYn) {
        Log.d(TAG,"productList, rollingYn 변경")
        if(productList.size>1&&rollingYn=="Y") {
            while(true) {
                delay(5000)
                Log.d(TAG,"productIndex:$productIndex, displayedProducts:$displayedProducts")
                productIndex = (productIndex + 1) % productList.size
                displayedProducts = productList.subList(productIndex,
                    minOf(productIndex + 1, productList.size))
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        displayedProducts.forEach { item ->
            MenuItemCard(item = item)
        }
    }
}

private val TitleColor = Color(0xFF4E3629)
private val SubTitleColor = Color(0xFF78909C)
private val PriceColor = Color(0xFF78909C)
private val CardBackgroundColor = Color.White
private val DividerColor = Color(0xFFEEEEEE)

@Composable
fun MenuItemCard(
    item: ProductVo,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CardBackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFFAFAF7))
                        .padding(start = 24.dp, end = 32.dp, top = 24.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.itemNm,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = TitleColor,
                        lineHeight = 60.sp,
                        maxLines = 2
                    )

                    item.itemNmEn?.let {
                        if (it.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = it,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Medium,
                                color = SubTitleColor,
                                maxLines = 1
                            )
                        }
                    }
                }

                Divider(
                    color = DividerColor,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                // 하단 (회색)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFF1F3EF))
                        .padding(start = 24.dp, end = 92.dp, top = 32.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = "${item.price.formatCurrency()}원",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = PriceColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(
                            if (item.imgPath.isNullOrBlank()) R.drawable.no_image
                            else item.imgPath
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = item.itemNm,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(SlightCurveShape)
                )
            }
        }
    }
}

private val SlightCurveShape = GenericShape { size: Size, _: LayoutDirection ->
    val curveDepth = size.width * 0.15f

    moveTo(curveDepth, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(curveDepth, size.height)

    quadraticBezierTo(
        x1 = -curveDepth * 0.2f,
        y1 = size.height / 2f,
        x2 = curveDepth,
        y2 = 0f
    )
    close()
}

private fun Int.formatCurrency(): String {
    return java.text.NumberFormat.getNumberInstance(java.util.Locale.KOREA).format(this)
}





@Preview(
    name = "SingleProduct Preview",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 1000,
    heightDp = 600,
)
@Composable
fun SingleProductPreview() {
    val previewProducts = listOf(
        ProductVo(
            itemNm = "아메리카노",
            itemNmEn = "Americano",
            price = 4500,
            imgPath = "preview_image"
        ),
        ProductVo(
            itemNm = "카페라떼",
            itemNmEn = "Caffe Latte",
            price = 5000,
            imgPath = "preview_image"
        )
    )

    SingleProduct(
        productList = previewProducts,
        rollingYn = "N"
    )
}
