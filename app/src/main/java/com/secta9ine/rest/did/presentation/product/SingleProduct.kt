package com.secta9ine.rest.did.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.secta9ine.rest.did.R

@Composable
fun SingleProduct() {
    Row() {
        Column(
            modifier = Modifier
                .padding(20.dp, 20.dp)
                .weight(1f)
        ) {
            Text(
                text = "탐라 흑돼지 김치찌개",
                fontSize = 50.sp
            )
            Text(
                text = "Tamra BlackPork Kimchi Jjigae",
                fontSize = 25.sp
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color.Magenta,
                thickness = 4.dp // 밑줄 두께
            )
            Text(
                text ="7,500원",
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = Color.Magenta,
                thickness = 4.dp // 밑줄 두께
            )

        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background((Color.White))
                .border(
                    width = 2.dp,
                    color = Color.Magenta
                )
        ) {
            Image(
                painter = rememberAsyncImagePainter("http://o2pos.spcnetworks.kr/files/pos/2022/04/04/1110/tmb_product_00F144.png"),
                contentDescription = "content",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}