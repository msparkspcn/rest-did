package com.secta9ine.rest.did.presentation.device

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppAlertDialog
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.util.UiString
private const val TAG = "DeviceScreen"
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeviceScreen(
    navController: NavHostController? = null,
    viewModel: DeviceViewModel = hiltViewModel()
) {
    var dialogMessage by remember { mutableStateOf<UiString?>(null) }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val uiState by viewModel.uiState.collectAsState(initial = DeviceViewModel.UiState.Idle)
//    val uiState by viewModel.uiState.collectAsState(initial = null)
    LaunchedEffect(uiState) {
        when(uiState) {
            is DeviceViewModel.UiState.Logout -> {
                navController?.popBackStack()
            }
            is DeviceViewModel.UiState.OrderStatus -> {
                navController?.navigate(Screen.OrderStatusScreen.route)
            }
            is DeviceViewModel.UiState.Error -> {
                Log.d(TAG,"Error message:${UiString.TextString((uiState as DeviceViewModel.UiState.Error).message)}")
                dialogMessage = UiString.TextString((uiState as DeviceViewModel.UiState.Error).message)
            }

            else -> {}
        }
    }

//    LaunchedEffect(Unit) {
//        viewModel.uiState.collect {
//            when(it) {
//                is DeviceViewModel.UiState.Logout -> {
//                    navController?.popBackStack()
//                }
//                is DeviceViewModel.UiState.OrderStatus -> {
//                    navController?.navigate(Screen.OrderStatusScreen.route)
//                }
//                is DeviceViewModel.UiState.Error -> {
//                    Log.d(TAG,"Error message:${it.message}")
//                    dialogMessage = UiString.TextString(it.message)
//                }
//
//                else -> {}
//            }
//        }
//    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                        viewModel.onBackSpacePressed()
                        Toast
                            .makeText(context, "로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT)
                            .show()
                        true
                    } else {
                        Toast
                            .makeText(context, "다른키 key pressed!", Toast.LENGTH_SHORT)
                            .show()
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
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .padding(25.dp, 10.dp),
                    style = TextStyle(fontSize = 17.sp),
                    color = Color(0xFF6F777D),
                )
            }

//        DeviceInfo(
//            infoNm = stringResource(id = R.string.cmp_nm),
//            infoList = viewModel.cmpNmList,
//            dividerUse = true
//        )
            TempDeviceInfo(
                infoNm = stringResource(id = R.string.cmp_nm),
                infoList = viewModel.cmpNmList,
                dividerUse = true
            )

            DeviceInfo(
                infoNm = stringResource(id = R.string.sales_org_nm),
                infoList = viewModel.salesOrgNmList,
                dividerUse = true,

                )

            DeviceInfo(
                infoNm = stringResource(id = R.string.store_nm),
                infoList = viewModel.storNmList,
                dividerUse = true
            )
            DeviceInfo(
                infoNm = stringResource(id = R.string.corner_nm),
                infoList = viewModel.cornerNmList,
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
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .padding(25.dp, 10.dp),
                    style = TextStyle(fontSize = 17.sp),
                    color = Color(0xFF6F777D),
                )
            }
            DeviceInfo(
                infoNm = stringResource(id = R.string.device_no),
                infoList = viewModel.deviceNoList,
                dividerUse = true
            )
            DeviceInfo(
                infoNm = stringResource(id = R.string.show_menu),
                infoList = viewModel.displayMenuList,
                dividerUse = true
            )
//        Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "롤링 : ",
                    modifier = Modifier.fillMaxWidth(0.2f),
                    style = TextStyle(fontSize = 19.sp)
                )
                RadioButton(
                    selected = viewModel.selectedOption == "fixed",
                    onClick = { viewModel.onSelectOption("fixed") }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.rolling_fix),
                    style = TextStyle(fontSize = 19.sp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                RadioButton(
                    selected = viewModel.selectedOption == "rolling",
                    onClick = { viewModel.onSelectOption("rolling")}
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.rolling_rolling),
                    style = TextStyle(fontSize = 19.sp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F6F8))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
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
        if(dialogMessage != null) {
            AppAlertDialog {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(0.6f)
                        .background(Color.White),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 텍스트를 가운데 정렬
                        Column(
                            modifier = Modifier
                                .weight(1f) // 남은 공간을 차지하도록 설정
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "알림",
                                style = TextStyle(fontSize = 24.sp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = dialogMessage?.asString() ?: "",
                                style = TextStyle(fontSize = 18.sp)
                            )
                        }

                        // 버튼을 하단에 배치
                        AppButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {},
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.confirm),
                                style = TextStyle(fontSize = 28.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun TempDeviceInfo(
    infoNm: String,
    infoList: List<Pair<String, String>>,
    dividerUse : Boolean
) {
    Log.d("TAG","infoList:$infoList")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 8.dp),
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

            if(infoList.isNotEmpty()) {
                TempDeviceDropdownMenuBox(
                    modifier = Modifier,
                    selectedItem = infoList[0].second,
                    itemList = infoList,
                    onSelectItem = {}
                )
            }
        }
    }
    if(dividerUse) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 5.dp),
            color = Color(0xFFCFD4D8),
            thickness = 1.dp
        )
    }
}

@Composable
fun DeviceInfo(
    infoNm: String,
    infoList: List<String>,
    dividerUse : Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp, 8.dp),
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

            if(infoList.isNotEmpty()) {
                DeviceDropdownMenuBox(
                    modifier = Modifier,
                    selectedItem = infoList[0],
                    itemList = infoList,
                    onSelectItem = {}
                )
            }
        }
    }
    if(dividerUse) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 5.dp),
            color = Color(0xFFCFD4D8),
            thickness = 1.dp
        )
    }
}

@Composable
fun TempDeviceDropdownMenuBox(
    modifier: Modifier = Modifier,
    selectedItem: String = "",
    itemList: List<Pair<String,String>> = emptyList(),
    onSelectItem: (index: Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .background(Color(0xFFE1E1E1))
                .border(1.dp, Color(0xFFA1A1A1))
                .padding(6.dp)
        ) {
            Text(
                text = selectedItem,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (expanded) 180f else 360f),
                tint = Color(0xFFA1A1A1),
            )
        }
        DropdownMenu(
            modifier = Modifier.wrapContentSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        onSelectItem(index)
                        expanded = false
                    }
                ) {
                    Text(text = item.second)
                }
            }
        }
    }
}

@Composable
fun DeviceDropdownMenuBox(
    modifier: Modifier = Modifier,
    selectedItem: String = "",
    itemList: List<String> = emptyList(),
    onSelectItem: (index: Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .background(Color(0xFFE1E1E1))
                .border(1.dp, Color(0xFFA1A1A1))
                .padding(6.dp)
        ) {
            Text(
                text = selectedItem,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .rotate(if (expanded) 180f else 360f),
                tint = Color(0xFFA1A1A1),
            )
        }
        DropdownMenu(
            modifier = Modifier.wrapContentSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        onSelectItem(index)
                        expanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}