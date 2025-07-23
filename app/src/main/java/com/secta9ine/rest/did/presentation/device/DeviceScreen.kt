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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.navigation.NavUtils.navigateAsSecondScreen
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppAlertDialog
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.util.CommonUtils
import com.secta9ine.rest.did.util.Const
import com.secta9ine.rest.did.util.UiString
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val TAG = "DeviceScreen"
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeviceScreen(
    navController: NavHostController? = null,
    viewModel: DeviceViewModel = hiltViewModel(),
    viewModel2: WebSocketViewModel = hiltViewModel()
) {
    var dialogMessage by remember { mutableStateOf<UiString?>(null) }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val uiState by viewModel.uiState.collectAsState(initial = DeviceViewModel.UiState.Init)
//    val uiState by viewModel.uiState.collectAsState(initial = null)
    val uiState2 by viewModel2.uiState.collectAsState(initial = WebSocketViewModel.UiState.Idle)

    var salesOrgNmList by remember (viewModel.salesOrgNmList) {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        viewModel.reload()
        coroutineScope {
            launch {
                viewModel.uiState.collect {
                    when(it) {
                        is DeviceViewModel.UiState.Logout -> {
                            navController?.popBackStack()
                        }
                        is DeviceViewModel.UiState.NavigateToOrderStatus -> {
                            navController?.navigateAsSecondScreen(Screen.OrderStatusScreen.route)
                        }
                        is DeviceViewModel.UiState.NavigateToProduct -> {
                            navController?.navigateAsSecondScreen(Screen.ProductScreen.route)
                        }
                        is DeviceViewModel.UiState.Error -> {
                            Log.d(TAG,"Error message:${UiString.TextString(it.message)}")
                            dialogMessage = UiString.TextString((it.message))
                        }

                        else -> {}
                    }
                }

            }
        }
    }
    LaunchedEffect(uiState2) {
        when (uiState2) {
            is WebSocketViewModel.UiState.UpdateDevice -> {
                Log.d(TAG, "여기서도 업데이트")

            }

            else -> {

            }
        }
    }

    if(uiState is DeviceViewModel.UiState.Init) {

    }
    else {
        Log.d(TAG,"current uiState:$uiState")
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
                    text = "환경 설정",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // 항목 간의 간격
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F6F8))
                                .padding(vertical = 5.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_info),
                                modifier = Modifier
                                    .fillMaxWidth(0.2f)
                                    .padding(25.dp, 10.dp),
                                style = TextStyle(fontSize = 17.sp),
                                color = Color(0xFF6F777D),
                            )
                        }
                    }
                    item {
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
                                    text = stringResource(id = R.string.version_info),
                                    style = TextStyle(fontSize = 19.sp)
                                )
                            }
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = CommonUtils.getAppVersion(context),
                                    style = TextStyle(fontSize = 19.sp)
                                )
                            }
                        }
                    }
                    item {
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
                    }
                    item {
                        TempDeviceInfo(
                            infoNm = stringResource(id = R.string.cmp_nm),
                            infoIdx = viewModel.cmpNmList.indexOfFirst
                            {
                                Log.d(TAG,"${it.first}, cmpCd:${viewModel.cmpCd}")
                                it.first == viewModel.cmpCd
                                                                       },
                            infoList = viewModel.cmpNmList,
                            dividerUse = true,
                            infoDiv = Const.DIV_CMP,
                            onSelectInfo = {
                                viewModel.cmpCd = viewModel.cmpNmList.getOrNull(it)?.first ?: ""
//                                viewModel.getCmpList()
//                            cmpCd ->
//                            viewModel.getCornerList(cmpCd, "8000", "")
                            }
                        )
                    }
                    item {
                        TempDeviceInfo(
                            infoNm = stringResource(id = R.string.sales_org_nm),
                            infoIdx = viewModel.salesOrgNmList.indexOfFirst {it.first == viewModel.salesOrgCd},
                            infoList = viewModel.salesOrgNmList,
                            dividerUse = true,
                            infoDiv = Const.DIV_SALESORG,
                            onSelectInfo = {
                                Log.d(TAG,"salesOrg it:$it")
                                viewModel.salesOrgCd = viewModel.salesOrgNmList.getOrNull(it)?.first ?:""
//                                salesOrgNmList = viewModel.salesOrgNmList.get(it)
                                viewModel.getStorList("", viewModel.salesOrgNmList[it].first)
//                                salesOrgCd ->
//                                viewModel.getStorList(cmpCd, s)
                            }
                        )
                    }
                    item {
                        TempDeviceInfo(
                            infoNm = stringResource(id = R.string.store_nm),
                            infoIdx = viewModel.storNmList.indexOfFirst { it.first == viewModel.storCd },
                            infoList = viewModel.storNmList,
                            dividerUse = true,
                            infoDiv = Const.DIV_STORE,
                            onSelectInfo = {
                                Log.d(TAG,"stor it:$it")
                                viewModel.storCd = viewModel.storNmList.getOrNull(it)?.first ?:""
                            }
                        )
                    }

                    item {
                        TempDeviceInfo(
                            infoNm = stringResource(id = R.string.corner_nm),
                            infoIdx = viewModel.cornerNmList.indexOfFirst { it.first == viewModel.cornerCd },
                            infoList = viewModel.cornerNmList,
                            dividerUse = false,
                            infoDiv = Const.DIV_CORNER,
                            onSelectInfo = {
                                viewModel.cornerCd = viewModel.cornerNmList.getOrNull(it)?.first ?:""
//                            cornerCd ->
//                            viewModel.getDeviceList(cornerCd)
                            }
                        )
                    }

                    item {
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
                    }
                    item {
                        DeviceInfo(
                            infoNm = stringResource(id = R.string.device_no),
                            infoList = viewModel.deviceNoList,
                            dividerUse = true
                        )
                    }
                    item {

                        DeviceInfo(
                            infoNm = stringResource(id = R.string.show_menu),
                            infoList = viewModel.displayMenuList,
                            dividerUse = true
                        )
                    }
                    item {
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
                                onClick = { viewModel.onSelectOption("fixed") },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(R.string.rolling_fix),
                                style = TextStyle(fontSize = 19.sp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            RadioButton(
                                selected = viewModel.selectedOption == "rolling",
                                onClick = { viewModel.onSelectOption("rolling") },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(R.string.rolling_rolling),
                                style = TextStyle(fontSize = 19.sp)
                            )
                        }
                    }

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
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "알림",
                                    style = TextStyle(fontSize = 30.sp)
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                Text(
                                    text = dialogMessage?.asString() ?: "",
                                    style = TextStyle(fontSize = 25.sp)
                                )
                            }

                            // 버튼을 하단에 배치
                            AppButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = viewModel::onLogout,
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

}



@Composable
fun TempDeviceInfo(
    infoNm: String,
    infoIdx: Int,
    infoList: List<Pair<String, String>>,
    dividerUse : Boolean,
    infoDiv: String,
    onSelectInfo: (index: Int) -> Unit = {}
) {
    Log.d("TAG","infoList:$infoList, infoIdx:$infoIdx")
    val adjustedInfoIdx = if (infoIdx == -1) 0 else infoIdx
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
                    selectedItem = infoList[adjustedInfoIdx].second,
                    itemList = infoList,
                    onSelectItem = {
//                        infoIdx = it
                        onSelectInfo(it)
                    }
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
    Log.d(TAG,"selctedItem:$selectedItem")
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
                        Log.d(TAG,"1.item:$item, index:$index")
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